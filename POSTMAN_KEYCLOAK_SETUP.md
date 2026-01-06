# Keycloak OAuth2 Setup Guide for Postman

## Keycloak Configuration Summary
- **Realm**: `helakatha-agri-realm`
- **Client ID**: `helaketha-agri`
- **Client Secret**: `SrNXIBfqswCdd5EkMIf46ZQ4jggoNo7V`
- **Issuer URI**: `http://localhost:8090`
- **Authorization Grant Type**: `authorization_code`
- **Redirect URI**: `http://localhost:8080` (or your Spring Boot app port)

---

## Method 1: Using Postman OAuth2 Authorization (Recommended)

### Step 1: Configure Postman Environment Variables

1. Open Postman → Click **Environments** (left sidebar)
2. Create a new environment or select existing one
3. Add these variables:

| Variable Name | Initial Value | Current Value |
|--------------|---------------|---------------|
| `keycloak_url` | `http://localhost:8090` | `http://localhost:8090` |
| `realm` | `helakatha-agri-realm` | `helakatha-agri-realm` |
| `client_id` | `helaketha-agri` | `helaketha-agri` |
| `client_secret` | `SrNXIBfqswCdd5EkMIf46ZQ4jggoNo7V` | `SrNXIBfqswCdd5EkMIf46ZQ4jggoNo7V` |
| `access_token` | (leave empty) | (will be auto-filled) |
| `base_url` | `http://localhost:8080` | `http://localhost:8080` |

### Step 2: Configure OAuth2 in Postman Request

1. Create a new request (e.g., `POST http://localhost:8080/api/farmers`)
2. Go to **Authorization** tab
3. Select **Type**: `OAuth 2.0`
4. Configure OAuth2 settings:

   **Configuration Options:**
   - **Grant Type**: `Authorization Code`
   - **Callback URL**: `http://localhost:8080` (or your redirect URI)
   - **Auth URL**: `{{keycloak_url}}/realms/{{realm}}/protocol/openid-connect/auth`
   - **Access Token URL**: `{{keycloak_url}}/realms/{{realm}}/protocol/openid-connect/token`
   - **Client ID**: `{{client_id}}`
   - **Client Secret**: `{{client_secret}}`
   - **Scope**: (leave empty or add `openid profile email`)
   - **State**: (leave empty)
   - **Client Authentication**: `Send as Basic Auth header` (recommended)

5. Click **Get New Access Token**
6. Postman will open a browser window for Keycloak login
7. After successful login, the token will be automatically added to your request
8. Click **Use Token** to use the generated token

### Step 3: Make Authenticated Requests

- The access token will be automatically included in the `Authorization` header as:
  ```
  Authorization: Bearer <access_token>
  ```
- You can now make requests to your secured endpoints

---

## Method 2: Manual Token Request (Alternative)

### Step 1: Get Authorization Code

1. Open browser and navigate to:
   ```
   http://localhost:8090/realms/helakatha-agri-realm/protocol/openid-connect/auth?client_id=helaketha-agri&redirect_uri=http://localhost:8080&response_type=code&scope=openid
   ```
2. Login with your Keycloak credentials
3. After login, you'll be redirected to: `http://localhost:8080?code=<AUTHORIZATION_CODE>`
4. Copy the `code` parameter from the URL

### Step 2: Exchange Authorization Code for Access Token

**Create a POST request in Postman:**

- **URL**: `http://localhost:8090/realms/helakatha-agri-realm/protocol/openid-connect/token`
- **Method**: `POST`
- **Headers**:
  ```
  Content-Type: application/x-www-form-urlencoded
  ```
- **Body** (x-www-form-urlencoded):
  ```
  grant_type: authorization_code
  client_id: helaketha-agri
  client_secret: SrNXIBfqswCdd5EkMIf46ZQ4jggoNo7V
  code: <AUTHORIZATION_CODE_FROM_STEP_1>
  redirect_uri: http://localhost:8080
  ```

**Response will contain:**
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJ...",
  "expires_in": 300,
  "refresh_expires_in": 1800,
  "refresh_token": "...",
  "token_type": "Bearer",
  "scope": "openid profile email"
}
```

### Step 3: Use Access Token in API Requests

1. Copy the `access_token` from the response
2. In your API request (e.g., `POST http://localhost:8080/api/farmers`)
3. Go to **Authorization** tab
4. Select **Type**: `Bearer Token`
5. Paste the token in the **Token** field
6. Make your request

---

## Method 3: Using Postman Pre-request Script (Automated)

### Step 1: Create a Token Request Collection

1. Create a new collection: "Keycloak Token"
2. Add a request: "Get Access Token"
3. Configure as Method 2, Step 2
4. In **Tests** tab, add:
   ```javascript
   if (pm.response.code === 200) {
       var jsonData = pm.response.json();
       pm.environment.set("access_token", jsonData.access_token);
       console.log("Access token saved to environment");
   }
   ```

### Step 2: Use Token in Other Requests

1. In your API request, go to **Authorization** tab
2. Select **Type**: `Bearer Token`
3. Token: `{{access_token}}`
4. The token will be automatically used from the environment variable

---

## Testing Your API Endpoints

### Example: Create Farmer (POST)

**Request:**
- **Method**: `POST`
- **URL**: `http://localhost:8080/api/farmers`
- **Authorization**: `Bearer Token` (use token from above)
- **Headers**:
  ```
  Content-Type: application/json
  ```
- **Body** (raw JSON):
  ```json
  {
    "fullName": "Kusal Perera",
    "phone": "0771234567",
    "email": "kusal@example.com",
    "address": "No 45, Galle Road, Colombo",
    "nic": "199512345678",
    "username": "kusal95",
    "password": "securePassword123"
  }
  ```

### Example: Get All Farmers (GET)

**Request:**
- **Method**: `GET`
- **URL**: `http://localhost:8080/api/farmers`
- **Authorization**: `Bearer Token` (use token from above)

---

## Important Notes

1. **Token Expiration**: Access tokens typically expire in 5 minutes (300 seconds). You'll need to refresh or get a new token.

2. **Refresh Token**: Use the `refresh_token` to get a new access token:
   ```
   POST http://localhost:8090/realms/helakatha-agri-realm/protocol/openid-connect/token
   
   Body (x-www-form-urlencoded):
   grant_type: refresh_token
   client_id: helaketha-agri
   client_secret: SrNXIBfqswCdd5EkMIf46ZQ4jggoNo7V
   refresh_token: <REFRESH_TOKEN>
   ```

3. **Keycloak User Setup**: Make sure you have users created in Keycloak realm `helakatha-agri-realm` with appropriate roles.

4. **CORS**: If you encounter CORS issues, ensure Keycloak has your frontend origin in the allowed CORS origins.

5. **Redirect URI**: The redirect URI in Keycloak client configuration must match exactly: `http://localhost:8080` (or your app port)

---

## Troubleshooting

### Error: "Invalid redirect_uri"
- Check that the redirect URI in Keycloak client matches exactly what you're using
- Go to Keycloak Admin Console → Clients → helaketha-agri → Valid Redirect URIs

### Error: "Invalid client credentials"
- Verify client_id and client_secret are correct
- Check that client authentication is enabled in Keycloak

### Error: "401 Unauthorized" when calling API
- Verify the access token is valid and not expired
- Check that the token is being sent in the Authorization header: `Bearer <token>`
- Verify the issuer-uri in application.yml matches your Keycloak realm

### Token not working
- Check token expiration time
- Verify the JWT decoder is pointing to the correct Keycloak realm
- Check Keycloak server is running on port 8090

---

## Quick Reference URLs

- **Keycloak Admin Console**: `http://localhost:8090/admin`
- **Realm Info**: `http://localhost:8090/realms/helakatha-agri-realm`
- **Authorization Endpoint**: `http://localhost:8090/realms/helakatha-agri-realm/protocol/openid-connect/auth`
- **Token Endpoint**: `http://localhost:8090/realms/helakatha-agri-realm/protocol/openid-connect/token`
- **JWK Set**: `http://localhost:8090/realms/helakatha-agri-realm/protocol/openid-connect/certs`

