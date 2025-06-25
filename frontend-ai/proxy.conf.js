const PROXY_CONFIG = [
  {
    context: ["/api"],
    target: "http://localhost:8080/",
    secure: false,
    logLevel: "debug",
  },
];

export default PROXY_CONFIG;
