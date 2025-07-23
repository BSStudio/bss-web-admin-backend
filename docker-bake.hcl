group "default" {
  targets = ["build"]
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
