# 📦 CDN Server

A minimal Spring Boot application for securely uploading, serving, and deleting static files. Designed for internal use with public `GET` access and protected `POST`/`DELETE` endpoints via an API key.

Hosted at: [https://cdn.devincoopers.space](https://cdn.devincoopers.space)

---

## 🚀 Features

- Upload images by app name via `POST /api/images/{app}`
- Serve public assets via `GET /{app}/{filename}`
- Delete images via `DELETE /api/images/{app}/{filename}`
- API key protection for write/delete actions
- Files stored on a persistent volume in `/app/uploads`

---

## 🧱 Tech Stack

- Java 21
- Spring Boot 3
- Docker + GHCR
- Kubernetes (K3s)
- Helm

---

## 🔐 API Security

Only `POST` and `DELETE` endpoints require an API key passed via the `x-api-key` header. The API key is injected via environment variable:

```
CDN_API_KEY=your_secret_key_here
```

This is stored securely in Kubernetes via a secret (`cdn-api-key-secret`).

---

## 📥 Upload Example

```bash
curl -X POST https://api.devincoopers.space/api/images/portfolio \
  -H "x-api-key: YOUR_API_KEY" \
  -F file=@screenshot.png
```

Response:
```
portfolio/1e083bc2-f23a-4ea0-9f52-screenshot.png
```

---

## 🧹 Delete Example

```bash
curl -X DELETE https://api.devincoopers.space/api/images/portfolio/1e083bc2-f23a-4ea0-9f52-screenshot.png \
  -H "x-api-key: YOUR_API_KEY"
```

---

## 🌐 Public Access

Public files are served at:
```
https://cdn.devincoopers.space/{app}/{filename}
```

No authentication required for public `GET`.

---

## 🐳 Docker

To build locally:
```bash
docker build -t cdn-server .
```

---

## ☸️ Helm Deployment

This service is deployed via the [`helm-values`](https://github.com/DARC-Software/helm-values) repository:

Helm chart path:
```
charts/shared/cdn-server
```

Set these values in `values.yaml`:
```yaml
image:
  repository: ghcr.io/darc-software/cdn-server
  tag: latest

env:
  - name: CDN_API_KEY
    valueFrom:
      secretKeyRef:
        name: cdn-api-key-secret
        key: CDN_API_KEY
```

Ingress is configured to support:
- `cdn.devincoopers.space` for public static file access
- `api.devincoopers.space/api/images` for internal API usage

---

## 👨‍💻 Maintainer

**Devin Cooper**  
[devincoopers.space](https://devincoopers.space)  
Email: looking@devincoopers.space
