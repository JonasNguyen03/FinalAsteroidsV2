module Core {
    requires Common;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.base;
    requires spring.context;
    requires spring.beans;
    requires spring.core;
    requires java.net.http;

    exports dk.sdu.cbse.main to javafx.graphics;
    opens dk.sdu.cbse.main to spring.core, spring.beans, spring.context, javafx.graphics;

    uses dk.sdu.cbse.common.services.IGamePluginService;
    uses dk.sdu.cbse.common.services.IEntityProcessingService;
    uses dk.sdu.cbse.common.services.IPostEntityProcessorService;
}