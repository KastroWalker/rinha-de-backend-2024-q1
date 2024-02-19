FROM alpine:3.15
RUN apk update && \
    apk add openjdk17 && \
    mkdir /application/
WORKDIR /application/
COPY build/libs/ .
EXPOSE 8080
CMD ["java", "-jar", "kastro.dev.rinha-de-backend-2024-q1-all.jar"]