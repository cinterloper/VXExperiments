
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.groovy.core.Vertx;
import io.vertx.groovy.core.buffer.Buffer

eb = vertx.eventBus()
ctx = vertx.getOrCreateContext()

class g{ /*globals*/
    public static u = [:] /*utils*/
    public static d = [:] /*database*/
}
g.u.log = LoggerFactory.getLogger("database.groovy");
g.u.cfg = ctx.config()

g.u.log.info("|database.groovy starting|\n")

def boolean readJson(name, cb){
    Vertx.vertx().fileSystem().readFile( 'json' + "/${name}", { result ->
        if (result.succeeded()) {
            cb(  new JsonObject(result.result().toString("ISO-8859-1")))

        } else {
            g.u.log.error("could not read json :${name}: " + result.cause())
        }
    })
}
def boolean writeJson(name,Buffer data,cb){
    Vertx.vertx().fileSystem().writeFile('json' + "/${name}", data, { result ->
        if (result.succeeded()) {
            g.u.log.info("wrote json for ${name} : ${data.toString("ISO-8859-1")}")
            cb(true)
        } else {
            g.u.log.error("could not write json :${name}: " + result.cause())
            cb(false)
        }
    })
}


dataReqChan = eb.consumer("dataRequest");
dataReqChan.handler( { message  ->
    resp =[:]
    g.u.log.info("got data request ${message.body()}")
    message.body()["t"].each({dtype ->
        g.u.log.info("db: processing data req for ${dtype}")
        readJson("${dtype}.json",{data ->
            resp[dtype]=data
            g.u.log.info("resp s : ${resp}")
            if(resp.size() == message.body()['t'].size())
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
    g.u.log.info("got data update request ${message.body()}")
    message.body()["t"].each({dtype ->
        g.u.log.info("db: processing data update req for ${dtype}")
        writeJson("${dtype}.json",Buffer.buffer(new JsonObject(message.body()["d"][dtype]).toString()) as Buffer,{ ar ->
            if(ar)
                g.u.log.info("wrote  "+dtype)
             message.reply(ar);
        })

    })
})


