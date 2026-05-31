## How to run

Open environment -> Run
> docker-compose -f environment/docker-compose-dev.yml up

Câu lệnh trên sẽ tự động tạo db với các thông số sau:
```bash
MYSQL_ROOT_PASSWORD: 123456
MYSQL_DATABASE: ticket
MYSQL_PASSWORD: 123456
```
Chú ý: Khi run thành công thi sẽ tự tạo một folder `data/db_data` trong `environment`