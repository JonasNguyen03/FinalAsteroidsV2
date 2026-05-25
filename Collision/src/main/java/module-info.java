module Collision {
    requires Common;
    requires spring.context;
    requires java.net.http;
    opens dk.sdu.cbse.collision to spring.core, spring.beans, spring.context;
}