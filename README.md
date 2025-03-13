# VaultEncryptor

VaultEncryptor is a Java library that integrates HashiCorp Vault to provide secure encryption and decryption of sensitive data. This library was developed to address the need for encrypting large files using Hashicorp Vault. It ensures secure key management by leveraging Vault's Transit secrets engine.

## Features
- Secure encryption and decryption using Vault
- Automatic retrieval of encryption keys from Vault
- Supports AES encryption with 256-bit keys
- Built with Gradle for easy dependency management

## Installation

### Manual Installation
Since this library is not published in a public repository, you need to clone the repository and build the `.jar` manually:

1. Clone the repository:
   ```sh
   git clone https://github.com/your-repo/vault-encryptor.git
   cd vault-encryptor
   ```
2. Build the project using Gradle:
   ```sh
   ./gradlew build
   ```
3. Add the generated `.jar` file to your project's dependencies.

For Gradle, add this to your `build.gradle`:
```gradle
dependencies {
    implementation files('libs/vault-encryptor-1.0.0.jar')
}
```

## Environment Variables
This library requires the following environment variables to be set:
- `VAULT_ADDR` - The address of your Vault server
- `VAULT_SECRET_TOKEN` - The authentication token for Vault

Ensure these variables are configured before using the library.

## Usage

This library encrypts text strings, so if you need to encrypt large files you need to first encode your file as Base64.

### Initialize the Vault Encryption Service

```java
VaultEncryptionService vaultService = new VaultEncryptionService();
```

### Encrypt Data
```java
String encryptedData = vaultService.encryptWithVault("Sensitive Data", "/path/to/keyfile.txt");
```

### Decrypt Data
```java
String decryptedData = vaultService.decryptWithVault(encryptedData, "/path/to/keyfile.txt");
```

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Credits
Developed by Mario Sánchez with contributions from Asinfo Software y Desarrollo.

