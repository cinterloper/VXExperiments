import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.groovy.core.Vertx;
import io.vertx.groovy.core.buffer.Buffer

eb = vertx.eventBus()
log = LoggerFactory.getLogger("database.groovy");

log.info("|database.groovy starting|\n")

def boolean readJson(name, cb){
    Vertx.vertx().fileSystem().readFile( 'json' + "/${name}", { result ->
        if (result.succeeded()) {
            cb(  new JsonObject(result.result().toString("ISO-8859-1")) ) //respond with the data from file
        } else {
            log.error("db.groovy: could not read json :${name}: " + result.cause())
        }
    })
}
def boolean writeJson(name,Buffer data,cb){
    Vertx.vertx().fileSystem().writeFile('json' + "/${name}", data, { result ->
        if (result.succeeded()) {
            log.info("db.groovy: wrote json for ${name} : ${data.toString("ISO-8859-1")}")
            cb(true)
        } else {
            log.error("db.groovy: could not write json :${name}: " + result.cause())
            cb(false)
        }
    })
}
dataReqChan = eb.consumer("dataRequest");
dataReqChan.handler( { message  ->

    def request = message.body()["t"]
    log.info("db.groovy:  processing data req for ${request}")

    readJson("${request}.json",  {data ->
                log.info("db.groovy: resp from readJson ${request} : ${data }")
                message.reply(data);
        })
})

dataUpdChan = eb.consumer("dataUpdate");
dataUpdChan.handler( { message  ->
    def request = message.body()["t"]
    def data = message.body()["d"]

    log.info("db.groovy: got data update request ${message.body()}")

    writeJson("${request}.json", Buffer.buffer(new JsonObject(data).toString()) ,{ ar ->
            if(ar)
                log.info("db.groovy: wrote  "+request)
	    else
		log.info("db.groovy: error writeing " + request)

            message.reply(ar);

        })
})


