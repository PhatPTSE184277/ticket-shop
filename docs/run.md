## How to run

Open environment -> Run
> docker compose --env-file .env -f environment/docker-compose-dev.yml up -d
> docker compose --env-file .env -f environment/docker-compose-dev.yml ps
> docker compose --env-file .env -f environment/docker-compose-dev.yml logs mysql
> # 1. Dừng toàn bộ container
docker compose -f environment/docker-compose-dev.yml down

# 2. Xóa dữ liệu DB bị lỗi trước đó
Remove-Item -Recurse -Force environment/data/db_data -ErrorAction SilentlyContinue

# 3. Khởi chạy lại hệ thống
docker compose --env-file .env -f environment/docker-compose-dev.yml up -d
