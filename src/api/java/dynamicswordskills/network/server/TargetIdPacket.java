/**
    Copyright (C) <2015> <coolAlias>

    This file is part of coolAlias' Dynamic Sword Skills Minecraft Mod; as such,
    you can redistribute it and/or modify it under the terms of the GNU
    General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.

    This program is distributed buffer the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package dynamicswordskills.network.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import cpw.mods.fml.relauncher.Side;
import dynamicswordskills.entity.DSSPlayerInfo;
import dynamicswordskills.network.AbstractMessage.AbstractServerMessage;
import dynamicswordskills.skills.ILockOnTarget;
import dynamicswordskills.skills.SkillBase;
import dynamicswordskills.util.LogHelper;

/**
 * 
 * This packet updates the server with the current target for currently active ILockOnTarget skill.
 *
 */
public class TargetIdPacket extends AbstractServerMessage<TargetIdPacket>
{
	/** Id of ILockOnTarget skill */
	private byte skillId;

	/** Current target from ILockOnTarget skill */
	private Entity targetEntity;

	/** Entity Id of above entity */
	private int entityId;

	/** True if current target entity is null */
	boolean isNull = false;

	public TargetIdPacket() {}

	/**
	 * Constructs packet that will update the provided skill's current target on the server
	 */
	public <T extends SkillBase & ILockOnTarget> TargetIdPacket(T skill) {
		this.skillId = skill.getId();
		this.targetEntity = ((ILockOnTarget) skill).getCurrentTarget();
	}

	@Override
	protected void read(PacketBuffer buffer) throws IOException {
		if (buffer.readByte() == 1) {
			this.skillId = buffer.readByte();
			this.entityId = buffer.readInt();
		} else {
			this.isNull = true;
		}
	}

	@Override
	protected void write(PacketBuffer buffer) throws IOException {
		if (targetEntity != null) {
			buffer.writeByte((byte) 1);
			buffer.writeByte(skillId);
			buffer.writeInt(targetEntity.getEntityId());
		} else {
			buffer.writeByte((byte) 0);
		}
	}

	@Override
	protected void process(EntityPlayer player, Side side) {
		ILockOnTarget skill = DSSPlayerInfo.get(player).getTargetingSkill();
		if (skill != null) {
			if (isNull) {
				skill.setCurrentTarget(player, null);
			} else {
				targetEntity = player.worldObj.getEntityByID(entityId);
				skill.setCurrentTarget(player, targetEntity);
				if (targetEntity == null) { // For some reason the target id is sometimes incorrect or out of date
					LogHelper.warn("Invalid target; entity with id " + entityId + " is null");
				}
			}
		}
	}
}
