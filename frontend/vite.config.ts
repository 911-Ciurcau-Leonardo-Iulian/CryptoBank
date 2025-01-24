import react from '@vitejs/plugin-react';
import { defineConfig } from 'vite';

export default defineConfig({
  server: {
    port: 3000,
    host: true,
    https: false,
    watch: {
      usePolling: false,
      useFsEvents: false
    },
    hmr: {
      overlay: false
    }
  },
  plugins: [
    react(),
  ],
  build: {
    outDir: 'build'
  },
  preview: {
    port: 3002,
    https: false,
    host: 'localhost',
  }
});
