#run
docker run --rm --name backupr-pg -e POSTGRES_PASSWORD=docker -d -p 5432:5432 -v $HOME/workspace/runtime/backupr/docker/volumes/postgres:/var/lib/postgresql/data postgres
