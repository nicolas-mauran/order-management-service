import { defineConfig } from "vite";

export default defineConfig({
  server: {
    port: 5173,
    proxy: {
      "/orders": "http://localhost:8080"
    }
  }
});
