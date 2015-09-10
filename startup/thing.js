


var logger = Java.type("io.vertx.core.logging.LoggerFactory").getLogger("thing.js");
logger.info("Launching js ...");
logger.info("env method: " + $ENV.METHOD);
var Buffer = require("vertx-js/buffer")
var Router = require("vertx-web-js/router")
var BodyHandler = require("vertx-web-js/body_handler");

var router = Router.router(vertx)

var method = $ENV.METHOD


if (method == 1) {
 try{
  vertx.createHttpServer().requestHandler(router.accept).listen(8080)
 } catch(e) {
   logger.error("could not launch server " + e) 
   vertx.close(function(res) {exit()})

 }
} else if (method ==2) {
  vertx.createHttpServer().listen(8080, "0.0.0.0", function(res, res_err) {
   if (res_err == null) {
	  logger.info("Listening on port: 8080")
   }
   else{ 
     logger.error('could not start http server, is the port available? :' + res_err + '\n');
     logger.error('shuting down server because we cannot setup the port\n');
     vertx.close(function(res) {exit()})
  }})  
} else if (method ==3) {
  vertx.createHttpServer().requestHandler(router.accept).listen(8080, "0.0.0.0", function(res, res_err) {
   if (res_err == null) {
	  logger.info("Listening on port: 8080")
   }
   else{ 
     logger.error('could not start http server, is the port available? :' + res_err + '\n');
     logger.error('shuting down server because we cannot setup the port\n');
     vertx.close(function(res) {exit()})
  }})  


}
  





