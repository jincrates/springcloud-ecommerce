$ cd keystore  

//비대칭키 생성
$ keytool -genkeypair -alias apiEncryptionKey -keyalg RSA -dname "CN=Jincrates, OU=API Development, O=jincrates.me, L=Seoul, C=KR" -keypass "1q2w3e4r" -keystore apiEncryptionKey.jks -storepass "1q2w3e4r"

//키 확인
$ keytool -list -keystore apiEncryptionKey.jks -v

//키 파일 export: 공개키 추출
keytool -export -alias apiEncryptionKey -keystore apiEncryptionKey.jks -rfc -file trustServer.cer

//키 파일 import
keytool -import -alias trustServer -file trustServer.cer -keystore publicKey.jks

//키 확인
keytool -list -keystore publicKey.jks
