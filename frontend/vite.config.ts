import react from '@vitejs/plugin-react';
import { defineConfig } from 'vite';
import {nodePolyfills} from "vite-plugin-node-polyfills";
import tailwindcss from "@tailwindcss/vite";

export default defineConfig({
  server: {
    port: 3000,
    host: true,
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
    nodePolyfills(),
    tailwindcss(),
  ],
  build: {
    outDir: 'build'
  },
  preview: {
    port: 3002,
    host: 'localhost',
  }
});
