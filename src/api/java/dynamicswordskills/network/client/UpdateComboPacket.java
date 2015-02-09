/**
    Copyright (C) <2015> <coolAlias>

    This file is part of coolAlias' Dynamic Sword Skills Minecraft Mod; as such,
    you can redistribute it and/or modify it under the terms of the GNU
    General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package dynamicswordskills.network.client;

import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import cpw.mods.fml.relauncher.Side;
import dynamicswordskills.entity.DSSPlayerInfo;
import dynamicswordskills.network.AbstractMessage.AbstractClientMessage;
import dynamicswordskills.skills.Combo;
import dynamicswordskills.skills.ICombo;
import dynamicswordskills.util.LogHelper;

/**
 * 
 * Packet responsible for keeping attack Combos synchronized between server and client.
 *
 */
public class UpdateComboPacket extends AbstractClientMessage<UpdateComboPacket>
{
	/** Stores data of combo to be updated */
	private NBTTagCompound compound;

	public UpdateComboPacket() {}

	public UpdateComboPacket(Combo combo) {
		compound = combo.writeToNBT();
	}

	@Override
	protected void read(PacketBuffer buffer) throws IOException {
		compound = buffer.readNBTTagCompoundFromBuffer();
	}

	@Override
	protected void write(PacketBuffer buffer) throws IOException {
		buffer.writeNBTTagCompoundToBuffer(compound);
	}

	@Override
	protected void process(EntityPlayer player, Side side) {
		Combo combo = Combo.readFromNBT(compound);
		try {
			ICombo skill = (ICombo) DSSPlayerInfo.get(player).getPlayerSkill(combo.getSkill());
			if (skill != null) {
				combo.getEntityFromWorld(player.worldObj);
				skill.setCombo(combo);
			}
		} catch (ClassCastException e) {
			LogHelper.warn("Class Cast Exception from invalid Combo skill id of " + combo.getSkill());
		}
	}
}
