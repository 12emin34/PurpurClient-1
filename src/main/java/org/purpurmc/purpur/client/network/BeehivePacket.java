package org.purpurmc.purpur.client.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;

public class BeehivePacket {
    public static final IntProperty NUM_OF_BEES = IntProperty.of("num_of_bees", 0, 256);

    public void requestBeehiveData(BlockPos pos) {
        ByteArrayDataOutput out = Packet.out();
        out.writeLong(pos.asLong());
        Packet.send(Packet.BEEHIVE_C2S, out);
    }

    public static void receiveBeehiveData(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        if (client.world == null) {
            return;
        }

        ByteArrayDataInput in = Packet.in(buf.getWrittenBytes());
        int count = in.readInt();
        long packedPos = in.readLong();
        BlockPos pos = BlockPos.fromLong(packedPos);

        BlockState state = client.world.getBlockState(pos);
        if (!state.contains(NUM_OF_BEES)) {
            return;
        }

        client.world.setBlockState(pos, state.with(NUM_OF_BEES, count));
    }
}
