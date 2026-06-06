## How to run

Open environment -> Run
> docker-compose -f environment/docker-compose-dev.yml up
> docker run --rm williamyeh/wrk -t5 -c100 -d2m http://host.docker.internal:8080/ticket/1/detail/1
> echo "GET http://localhost:8080/ticket/1/detail/1" | ./vegeta attack -name=2000qps -duration=10s -rate=100 > benchmark/results_2000qps.bin
> & "C:\Users\ASUS\.jdks\openjdk-23.0.1\bin\java.exe" -jar xxxx-start/target/xxxx-start-1.0-SNAPSHOT.jar --server.port=2233

Câu lệnh trên sẽ tự động tạo db với các thông số sau:
```bash
MYSQL_ROOT_PASSWORD: root1234
MYSQL_DATABASE: vetautet
MYSQL_PASSWORD: root1234
```
Chú ý: Khi run thành công thi sẽ tự tạo một folder `data/db_data` trong `environment`