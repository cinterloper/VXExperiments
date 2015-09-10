var logger = Java.type("io.vertx.core.logging.LoggerFactory").getLogger("server.js");
logger.info("|begin game.js|");

var Buffer = require("vertx-js/buffer")
var eb = vertx.eventBus()
var fs = vertx.fileSystem()

var ctx = vertx.getOrCreateContext()
var config = ctx.config()
var someData = {}
var textureWorld = {}

function reqData(types,ctx,cb){
  logger.info("sending data request of types : " + JSON.stringify(types))
  eb.send("dataRequest", {"t":types,"c":ctx} , function (ar, ar_err) {
    if (ar_err == null) {
      console.log("Received reply: " + ar.body());
      cb(ar.body())
    }
  });
}

function updateData(types,data,ctx,cb){
  logger.info("sending data request of types : " + JSON.stringify(types))
  eb.send("dataUpdate", {"t":types,"d":data,"c":ctx} , function (ar, ar_err) {
    if (ar_err == null) {
      console.log("Received reply: " + ar.body());
      cb(ar.body())
    }
  });
}

function InfoRequest() {
logger.info("saving out some data: " )
reqData(['world','textureWorld'],{ "loc":{"x":0,"y":0,"z":0},"user":"root"},function(Jdata) {
          someData = Jdata['world']
          textureWorld=Jdata['textureWorld']
          logger.info("got jdata: "+JSON.stringify(Jdata,null,2))
          logger.info("game.js got data req resp on new player join " + Object.keys(Jdata))
              updateData( ["world"], {"world":someData}, {"loc":{"x":0,"y":0,"z":0},"user":"root"}, function( ar ) {
                    if(ar)
                      logger.info('/world saved via eb')
                } )
                updateData( ["textureWorld"], {"textureWorld":textureWorld}, {"loc":{"x":0,"y":0,"z":0},"user":"root"}, function( ar ) {
                    if(ar)
                      logger.info('/textureWorld saved via eb')
                } )

        })



}
vertx.setPeriodic(1000, function (v) {
   logger.info("makeing request")
   InfoRequest();
});