module.exports = {
  parser: '@typescript-eslint/parser',
  env: {
    node: true,
    jest: true,
  },
  parserOptions: {
    sourceType: 'module',
  },
  extends: [
    'prettier',
    'plugin:prettier/recommended',
    'plugin:jest/all',
    'plugin:@typescript-eslint/recommended',
  ],
  plugins: ['prettier', 'jest'],
  rules: {
    'jest/no-hooks': 'off',
  },
}
