var logger = Java.type("io.vertx.core.logging.LoggerFactory").getLogger("server.js");
logger.info("|begin game.js|");

var Buffer = require("vertx-js/buffer")
var eb = vertx.eventBus()
var fs = vertx.fileSystem()
var someData = {
    "not": "null"
}

function reqData(type, cb) {
    logger.info("game.js: sending data request of type : " + JSON.stringify(type))

    eb.send("dataRequest", {
        "t": type
    }, function(ar, ar_err) {
        if (ar_err == null) {
            logger.info("game.js: Received reqData reply: " + JSON.stringify(ar.body()));
            cb(ar.body())
        }
    });

}

function updateData(type, data, cb) {
    logger.info("game.js: sending data request of type : " + JSON.stringify(type))

    eb.send("dataUpdate", {"t": type, "d": data }, function(ar, ar_err) {
        if (ar_err == null) {
            logger.info("game.js: Received updateData reply: " + JSON.stringify(ar.body()));
            cb(ar.body())
        }
    });

}

function InfoRequest() {
    logger.info("game.js: nested read / write request")

    /*read some data*/
    reqData('world', function(Jdata) {
        someData = Jdata
        logger.info("game.js : got jdata: " + JSON.stringify(Jdata))

        /*write it back*/
        updateData("world", someData , function(ar) {
            if (ar)
                logger.info('game.js : /world saved via eb')
        })
    })

}
vertx.setPeriodic(1000, function(v) {
    logger.info("game.js : makeing request")
    InfoRequest();
});
