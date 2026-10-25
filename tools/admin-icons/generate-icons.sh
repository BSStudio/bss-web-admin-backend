#!/usr/bin/env bash
set -euo pipefail

script_dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
repo_root="$(cd -- "$script_dir/../.." && pwd)"
icons_dir="$repo_root/server/ui/src/main/resources/static/icons"
html_data="$(mktemp)"
trap 'rm -f "$html_data"' EXIT

# CI reruns this generator and checks that the generated assets stay in sync.
npx --yes realfavicon@0.9.3 generate \
  "$script_dir/logo.svg" \
  "$script_dir/favicon-settings.json" \
  "$html_data" \
  "$icons_dir"

cp "$script_dir/logo.svg" "$icons_dir/logo.svg"
cp "$script_dir/logo-horizontal.svg" "$icons_dir/logo-horizontal.svg"
