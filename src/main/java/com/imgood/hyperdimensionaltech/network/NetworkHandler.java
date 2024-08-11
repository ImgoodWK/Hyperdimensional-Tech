package com.imgood.hyperdimensionaltech.network;

import com.imgood.hyperdimensionaltech.entity.EntitySwordBeam;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

import static com.imgood.hyperdimensionaltech.utils.Enums.Names.MOD_ID;

/**
 * @program: Hyperdimensional-Tech
 * @description:
 * @create: 2024-08-12 14:48
 **/
public class NetworkHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID );

    public static void init() {
        INSTANCE.registerMessage(MessageSwordBeamSpawn.Handler.class, MessageSwordBeamSpawn.class, 0, Side.CLIENT);
    }

    public static class MessageSwordBeamSpawn implements IMessage {
        private int entityId;
        private double x, y, z;
        private float yaw, pitch;

        public MessageSwordBeamSpawn() {}

        public MessageSwordBeamSpawn(EntitySwordBeam entity) {
            this.entityId = entity.getEntityId();
            this.x = entity.posX;
            this.y = entity.posY;
            this.z = entity.posZ;
            this.yaw = entity.rotationYaw;
            this.pitch = entity.rotationPitch;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            entityId = buf.readInt();
            x = buf.readDouble();
            y = buf.readDouble();
            z = buf.readDouble();
            yaw = buf.readFloat();
            pitch = buf.readFloat();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(entityId);
            buf.writeDouble(x);
            buf.writeDouble(y);
            buf.writeDouble(z);
            buf.writeFloat(yaw);
            buf.writeFloat(pitch);
        }

        public static class Handler implements IMessageHandler<MessageSwordBeamSpawn, IMessage> {
            @Override
            public IMessage onMessage(final MessageSwordBeamSpawn message, MessageContext ctx) {
                if (ctx.side == Side.CLIENT) {
                    Minecraft.getMinecraft().func_152344_a(new Runnable() {
                        @Override
                        public void run() {
                            World world = Minecraft.getMinecraft().theWorld;
                            EntitySwordBeam entity = new EntitySwordBeam(world);
                            entity.setEntityId(message.entityId);
                            entity.setPosition(message.x, message.y, message.z);
                            entity.rotationYaw = message.yaw;
                            entity.rotationPitch = message.pitch;
                            world.spawnEntityInWorld(entity);
                        }
                    });
                }
                return null;
            }
        }
    }
}
