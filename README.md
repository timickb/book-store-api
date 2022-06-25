# Book Store API

REST API для книжного магазина

## Сборка и запуск

Требуемая версия Java - 18.

`mvn clean install` - сгенерирует JAR with dependencies.


`java -jar target/bookstore-api.jar [data.json] [logs.txt]` - запускает
сервер. Опционально принимает первым аргументом путь к JSON файлу с данными, которые
будут занесены в базу данных при запуске, вторым аргументом -
путь к файлу для записи логов.

Пример содержимого JSON файла:

```json
{
    "accounts": [
        {
            "email": "somebody@somewhere.com",
            "password": "12345678",
            "balance": 100
        }
    ],
    "books": [
        {
            "author": "Стив Макконелл",
            "name": "Совершенный код",
            "price": 1000,
            "amount": 7
        }
    ]
}
```

### Docker
Для сбрки образа в директории `target` должен находиться созданный на предыдущем шаге JAR файл.
Пути к файлам `data.json` и `logs.txt` прописаны в аргументах запуска в `Dockerfile`.

`docker-compose up` - собирает и запускает сервер вместе с базой
данных.