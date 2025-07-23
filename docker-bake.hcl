group "default" {
  targets = ["build"]
}

target "docker-metadata-action" {
  # target will be overridden with docker/metadata-action output
  # see: https://github.com/docker/metadata-action?tab=readme-ov-file#bake-definition
}

target "build" {
  inherits = ["docker-metadata-action"]
  context    = "./"
  dockerfile = "Dockerfile"
  platforms = [
    "linux/amd64",
    # "linux/arm64",
  ]
}
