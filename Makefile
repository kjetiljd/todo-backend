# Configurable variables
IMAGE ?= todo-backend:latest
PORT ?= 8080
CONTAINER_NAME ?= todo-backend

.PHONY: $(shell sed -n -e '/^$$/ { n ; /^[^ .\#][^ ]*:/ { s/:.*$$// ; p ; } ; }' $(MAKEFILE_LIST))

help: # Extracts make targets with doble-hash comments and prints them
	@grep -hE '^\S+:.*##' $(MAKEFILE_LIST) | sed -e 's/:.*##\s*/ : /' | while IFS=' : ' read -r cmd desc; do \
		printf "\033[36m%-20s\033[0m %s\n" "$$cmd" "$$desc"; \
	done

build: ## Build application (fat jar)
	./gradlew bootJar --no-daemon

test: ## Run unit tests
	./gradlew test --no-daemon

run: ## Start the service locally (uses PORT env if set)
	./gradlew bootRun --no-daemon --args='--server.port=$(PORT)'

build-docker: ## Build Docker image
	docker build -t $(IMAGE) .

run-docker: build-docker ## Build and run container
	docker run --rm -p $(PORT):8080 --name $(CONTAINER_NAME) -v $(PWD)/data:/app/data $(IMAGE)

run-docker-bg: build-docker ## Build and run container in background
	docker run -d --rm -p $(PORT):8080 --name $(CONTAINER_NAME) -v $(PWD)/data:/app/data $(IMAGE)

stop-docker: ## Stop and remove the running container
	docker stop $(CONTAINER_NAME) || true
	docker rm $(CONTAINER_NAME) || true
