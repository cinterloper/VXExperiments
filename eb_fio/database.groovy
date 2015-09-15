import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.groovy.core.Vertx;
import io.vertx.groovy.core.buffer.Buffer

eb = vertx.eventBus()
log = LoggerFactory.getLogger("database.groovy");

log.info("|database.groovy starting|\n")

def boolean readJson(name, cb){
    vertx.fileSystem().readFile( 'json' + "/${name}", { result ->
        if (result.succeeded()) {
            cb(  new JsonObject(result.result().toString("ISO-8859-1")) ) //respond with the data from file
        } else {
            log.error("db.groovy: could not read json :${name}: " + result.cause())
        }
    })
}

vertx.setPeriodic(1000, { v ->
    readJson("world.json",  {data ->
                log.info("db.groovy: resp from readJson world.json : ${data }")
        })
})

