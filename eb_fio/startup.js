var logger = Java.type("io.vertx.core.logging.LoggerFactory").getLogger("startup.js");
logger.info("|Starting up.|\n");
var eb = vertx.eventBus()
var fs = vertx.fileSystem()
var ctx = vertx.getOrCreateContext()
var config = ctx.config()
var launchgrp = {}
	var svrOpts = {
	 "config" : config
	};
	var dbOpts = {
	 "config" : config,
	   "worker" : true
	};

 launchgrp[0]={"v":"database.groovy","opts": dbOpts, "startReady":false}
 launchgrp[1]={"v":"game.js","opts":svrOpts, "startReady":false}



function deploy(ctx,lgrp) {

    //logger.info("lgrp: "+JSON.stringify(lgrp))
    for (var ele in lgrp)
     {
       logger.info("launching "+ JSON.stringify(lgrp[ele].v) + '\n')
       vertx.deployVerticle(lgrp[ele].v, lgrp[ele].opts, function(res, err)
       {
	     if (err) {
	       logger.error("deploy of " + err.printStackTrace() + " failed \n");
	     } else {
	       logger.info("deploy of " + JSON.stringify(res) + " completed \n");
	     }
	   })
    }

}
deploy({},launchgrp)