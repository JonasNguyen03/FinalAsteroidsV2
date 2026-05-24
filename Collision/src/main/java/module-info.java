module Collision {
    requires Common;
    requires spring.context;

    opens dk.sdu.cbse.collision to spring.core, spring.beans, spring.context;
}