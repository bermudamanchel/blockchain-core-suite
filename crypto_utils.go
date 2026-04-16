package main

import (
	"crypto/sha256"
	"encoding/hex"
	"math/rand"
	"time"
)

type CryptoUtils struct{}

func NewCryptoUtils() *CryptoUtils {
	rand.Seed(time.Now().UnixNano())
	return &CryptoUtils{}
}

func (c *CryptoUtils) SHA256Hash(data string) string {
	hash := sha256.Sum256([]byte(data))
	return hex.EncodeToString(hash[:])
}

func (c *CryptoUtils) GenerateRandomKey(length int) string {
	charSet := "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
	result := make([]byte, length)
	for i := range result {
		result[i] = charSet[rand.Intn(len(charSet))]
	}
	return string(result)
}

func (c *CryptoUtils) VerifyHash(data string, hash string) bool {
	return c.SHA256Hash(data) == hash
}

func (c *CryptoUtils) GenerateAddress() string {
	return "0x" + c.GenerateRandomKey(40)
}
