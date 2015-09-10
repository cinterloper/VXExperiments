var logger = Java.type("io.vertx.core.logging.LoggerFactory").getLogger("startup.js");
logger.info("|Starting up.|\n");

     vertx.deployVerticle('database.groovy', {}, function(res, err)
       {
	     if (err) {
	       logger.error("deploy of " + err.printStackTrace() + " failed \n");
	     } else {
	       logger.info("deploy of " + JSON.stringify(res) + " completed \n");
	     }
	   })


	      vertx.deployVerticle('game.js', {}, function(res, err)
              {
       	     if (err) {
       	       logger.error("deploy of " + err.printStackTrace() + " failed \n");
       	     } else {
       	       logger.info("deploy of " + JSON.stringify(res) + " completed \n");
       	     }
       	   })