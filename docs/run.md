## How to run

Open environment -> Run
> docker-compose -f environment/docker-compose-dev.yml up
> docker run --rm williamyeh/wrk -t5 -c100 -d2m http://host.docker.internal:8080/ticket/1/detail/1

Câu lệnh trên sẽ tự động tạo db với các thông số sau:
```bash
MYSQL_ROOT_PASSWORD: root1234
MYSQL_DATABASE: vetautet
MYSQL_PASSWORD: root1234
```
Chú ý: Khi run thành công thi sẽ tự tạo một folder `data/db_data` trong `environment`