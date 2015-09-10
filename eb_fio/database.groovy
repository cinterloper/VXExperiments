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
    def resp =[:]
    def requests = message.body()["t"]

    requests.each({reqType ->
        log.info("db.groovy:  processing data req for ${reqType}")

        readJson("${reqType}.json",  {data ->
            resp[reqType]=data
            log.info("db.groovy: resp from readJson ${reqType} : ${resp}")
            if(resp.size() == requests.size()) // all files requeted have been read, resp obj is populated
            {
                message.reply(new JsonObject(resp));
                resp = [:]
            }
        })

    })
})

dataUpdChan = eb.consumer("dataUpdate");
dataUpdChan.handler( { message  ->
    def resp =[:]
    def requests = message.body()["t"]
    log.info("db.groovy: got data update request ${message.body()}")
    requests.each({reqType ->
        log.info("db: processing data update req for ${reqType}")
        def data=message.body()["d"][reqType]
        writeJson("${reqType}.json", Buffer.buffer(new JsonObject(data).toString()) ,{ ar ->
            resp[reqType]=ar
            if(ar)
                log.info("db.groovy: wrote  "+reqType)
	    else
		log.info("db.groovy: error writeing " + reqType)
            if(resp.size() == requests.size()) // all files requeted have been written, resp obj is populated
            {
                message.reply(new JsonObject(resp));
                resp = [:]
            }
        })

    })
})


