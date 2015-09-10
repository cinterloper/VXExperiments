import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.groovy.ext.web.Router
import io.vertx.core.http.HttpMethod
import io.vertx.groovy.ext.web.handler.BodyHandler



def logger = LoggerFactory.getLogger("main.groovy")
def router = Router.router(vertx)
router.route().handler(BodyHandler.create())
def server = vertx.createHttpServer()

def method = System.getenv()['METHOD'].toInteger()
logger.info("using method: " + method)



if(method == 1) 
{
 try {
    server.requestHandler(router.&accept).listen(8080)
 } catch (e) {
    logger.error "could not setup http server:" + e.getMessage()
 }
} else if (method==2) {
  server.listen(8080, "localhost", { res ->
   if (res.succeeded()) {
    println("Server is now listening!")
   } else {
    println("Failed to bind!")
   }
  })
} else if (method==3) {
  server.requestHandler(router.&accept).listen(8080, "localhost", { res ->
   if (res.succeeded()) {
    println("Server is now listening!")
   } else {
    println("Failed to bind!")
   }
  })
} 







