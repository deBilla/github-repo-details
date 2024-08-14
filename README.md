# GitHub Repository Details Caching API

This API will call GitHub API and get details of a GitHub repository and return the details after caching it.

## Development

To setup the development environment you need `Java 21` installed and `Redis` installed.

And following environment variables should be set in your local environment

`SPRING_REDIS_HOST=localhost`
`SPRING_REDIS_HOST=6379`

Then run the following commands.

`git clone https://github.com/deBilla/github-repo-details`

`./mvnw clean install`

`./mvnw springboot:run`

## Deployment

Deployment could be done using `docker-compose`. For this your environment need to have `Docker` installed.

`docker-compose up`