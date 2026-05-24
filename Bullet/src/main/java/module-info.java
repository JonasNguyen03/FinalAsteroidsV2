module Bullet {
    requires Common;
    requires spring.context;

    opens dk.sdu.cbse.bullet to spring.core, spring.beans, spring.context;
}