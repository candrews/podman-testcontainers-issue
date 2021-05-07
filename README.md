See https://github.com/containers/podman/issues/10261

Test of `Testcontainers.exposeHostPorts`: https://www.testcontainers.org/features/networking/#exposing-host-ports-to-the-container

The test is: [`src/test/java/CurlToHostTest.java`](src/test/java/CurlToHostTest.java)

To run against docker: `./test-docker.sh` The test should pass.

To run against podman: `./test-podman.sh` As of podman 3.2.0-rc1, the test fails. Looking at the stdout of the failing test, the reason for the failure is:
```
[main] ERROR 🐳 [curlimages/curl:7.76.0] - Log output from the failed container:
curl: (6) Could not resolve host: host.testcontainers.internal
```
