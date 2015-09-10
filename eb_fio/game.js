var logger = Java.type("io.vertx.core.logging.LoggerFactory").getLogger("server.js");
logger.info("|begin game.js|");

var Buffer = require("vertx-js/buffer")
var eb = vertx.eventBus()
var fs = vertx.fileSystem()
var someData = {
    "not": "null"
}

function reqData(types, cb) {
    logger.info("game.js: sending data request of types : " + JSON.stringify(types))

    eb.send("dataRequest", {
        "t": types
    }, function(ar, ar_err) {
        if (ar_err == null) {
            logger.info("game.js: Received reqData reply: " + JSON.stringify(ar.body()));
            cb(ar.body())
        }
    });

}

function updateData(types, data, cb) {
    logger.info("game.js: sending data request of types : " + JSON.stringify(types))

    eb.send("dataUpdate", {
        "t": types,
        "d": data
    }, function(ar, ar_err) {
        if (ar_err == null) {
            logger.info("game.js: Received updateData reply: " + ar.body());
            cb(ar.body())
        }
    });

}



function InfoRequest() {
    logger.info("game.js: nested read / write request")

    /*read some data*/
    reqData(['world'], function(Jdata) {
        someData = Jdata['world']
        logger.info("game.js : got jdata: " + JSON.stringify(Jdata))

        /*write it back*/
        updateData(["world"], {
            "world": someData
        }, function(ar) {
            if (ar)
                logger.info('game.js : /world saved via eb')
        })
    })

}
vertx.setPeriodic(1000, function(v) {
    logger.info("game.js : makeing request")
    InfoRequest();
});