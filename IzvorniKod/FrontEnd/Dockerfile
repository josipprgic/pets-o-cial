FROM node:14.1-alpine AS builder

WORKDIR /opt/web
COPY package.json package-lock.json ./
RUN npm install

ENV PATH="./node_modules/.bin:$PATH"

COPY . ./
RUN npm run build

RUN export NODE_OPTIONS="--max-old-space-size=8192"

CMD ["npm", "start"]