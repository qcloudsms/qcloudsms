package qcloudsms

import (
	"crypto/sha256"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"math/rand"
	"net/http"
	"strings"
	"time"
)

func init() {
	rand.Seed(time.Now().Unix())
}

func getRandom() int64 {
	return rand.Int63n(999999)%900000 + 100000
}

func getTime() int64 {
	return time.Now().Unix()
}

func strToHash(str string) string {
	return fmt.Sprintf("%x", sha256.Sum256([]byte(str)))
}

func calculateSig(appkey string, random int64, curTime int64, phoneNumbers []string) string {
	phoneNumbersString := strings.Join(phoneNumbers, ",")
	rawText := fmt.Sprintf("appkey=%s&random=%d&time=%d&mobile=%s", appkey, random, curTime, phoneNumbersString)
	return strToHash(rawText)
}

func apiRequest(req *http.Request, result interface{}) error {
	client := &http.Client{
		Timeout:   10 * time.Second,
		Transport: http.DefaultTransport,
	}
	resp, err := client.Do(req)
	if err != nil {
		return fmt.Errorf("http error  %v", err)
	}
	defer resp.Body.Close()
	if resp.StatusCode != http.StatusOK {
		return fmt.Errorf("http error code %d", resp.StatusCode)
	}
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return fmt.Errorf("read http resp body error %v", err)
	}
	err = json.Unmarshal(body, &result)
	return err
}
