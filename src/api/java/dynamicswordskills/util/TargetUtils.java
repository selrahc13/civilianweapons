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

package dynamicswordskills.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


/**
 * 
 * A collection of methods related to target acquisition
 *
 */
public class TargetUtils
{
	/** Maximum range within which to search for targets */
	private static final int MAX_DISTANCE = 256;
	/** Max distance squared, used for comparing target distances (avoids having to call sqrt) */
	private static final double MAX_DISTANCE_SQ = MAX_DISTANCE * MAX_DISTANCE;

	/** Returns the player's current reach distance, taking held item into account if applicable */
	// Packet7UseEntity uses 36.0D for determining if an attack should hit, or 9.0D if the entity cannot be seen
	// EntityRenderer uses 36.0D for creative mode, otherwise 9.0D, in calculating whether mouseover entity should be null
	// but using this exactly results in some attacks that in reality hit, being counted as a miss
	// Unlike Creative Mode, the mouseover is always null when an attack should miss when in Survival
	public static double getReachDistanceSq(EntityPlayer player) {
		return 38.5D; // seems to be just about right for Creative Mode hit detection
	}

	/**
	 * Returns true if current target is within the player's reach distance; does NOT check mouse over
	 */
	public static boolean canReachTarget(EntityPlayer player, Entity target) {
		return (player.canEntityBeSeen(target) && player.getDistanceSqToEntity(target) < getReachDistanceSq(player));
	}

	/**
	 * Returns MovingObjectPosition of Entity or Block impacted, or null if nothing was struck
	 * @param entity	The entity checking for impact, e.g. an arrow
	 * @param shooter	An entity not to be collided with, generally the shooter
	 * @param hitBox	The amount by which to expand the collided entities' bounding boxes when checking for impact (may be negative)
	 * @param flag		Optional flag to allow collision with shooter, e.g. (ticksInAir >= 5)
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static MovingObjectPosition checkForImpact(World world, Entity entity, Entity shooter, double hitBox, boolean flag) {
		Vec3 vec3 = Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ);
		Vec3 vec31 = Vec3.createVectorHelper(entity.posX + entity.motionX, entity.posY + entity.motionY, entity.posZ + entity.motionZ);
		// func_147447_a is the ray_trace method
		MovingObjectPosition mop = world.func_147447_a(vec3, vec31, false, true, false);
		vec3 = Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ);
		vec31 = Vec3.createVectorHelper(entity.posX + entity.motionX, entity.posY + entity.motionY, entity.posZ + entity.motionZ);

		if (mop != null) {
			vec31 = Vec3.createVectorHelper(mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord);
		}

		Entity target = null;
		List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(entity, entity.boundingBox.addCoord(entity.motionX, entity.motionY, entity.motionZ).expand(1.0D, 1.0D, 1.0D));
		double d0 = 0.0D;
		//double hitBox = 0.3D;

		for (int i = 0; i < list.size(); ++i) {
			Entity entity1 = (Entity) list.get(i);
			if (entity1.canBeCollidedWith() && (entity1 != shooter || flag)) {
				AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(hitBox, hitBox, hitBox);
				MovingObjectPosition mop1 = axisalignedbb.calculateIntercept(vec3, vec31);
				if (mop1 != null) {
					double d1 = vec3.distanceTo(mop1.hitVec);
					if (d1 < d0 || d0 == 0.0D) {
						target = entity1;
						d0 = d1;
					}
				}
			}
		}

		if (target != null) {
			mop = new MovingObjectPosition(target);
		}

		if (mop != null && mop.entityHit instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) mop.entityHit;
			if (player.capabilities.disableDamage || (shooter instanceof EntityPlayer
					&& !((EntityPlayer) shooter).canAttackPlayer(player)))
			{
				mop = null;
			}
		}

		return mop;
	}

	/**
	 * Returns true if the entity is directly in the crosshairs
	 */
	@SideOnly(Side.CLIENT)
	public static boolean isMouseOverEntity(Entity entity) {
		MovingObjectPosition mop = Minecraft.getMinecraft().objectMouseOver;
		return (mop != null && mop.entityHit == entity);
	}

	/**
	 * Returns the Entity that the mouse is currently over, or null
	 */
	@SideOnly(Side.CLIENT)
	public static Entity getMouseOverEntity() {
		MovingObjectPosition mop = Minecraft.getMinecraft().objectMouseOver;
		return (mop == null ? null : mop.entityHit);
	}

	/** Returns the EntityLivingBase closest to the point at which the seeker is looking and within the distance and radius specified */
	public static final EntityLivingBase acquireLookTarget(EntityLivingBase seeker, int distance, double radius) {
		return acquireLookTarget(seeker, distance, radius, false);
	}

	/**
	 * Returns the EntityLivingBase closest to the point at which the entity is looking and within the distance and radius specified
	 * @param distance max distance to check for target, in blocks; negative value will check to MAX_DISTANCE
	 * @param radius max distance, in blocks, to search on either side of the vector's path
	 * @param closestToEntity if true, the target closest to the seeker and still within the line of sight search radius is returned
	 * @return the entity the seeker is looking at or null if no entity within sight search range
	 */
	@SuppressWarnings("unchecked")
	public static final EntityLivingBase acquireLookTarget(EntityLivingBase seeker, int distance, double radius, boolean closestToSeeker) {
		if (distance < 0 || distance > MAX_DISTANCE) {
			distance = MAX_DISTANCE;
		}
		EntityLivingBase currentTarget = null;
		double currentDistance = MAX_DISTANCE_SQ;
		Vec3 vec3 = seeker.getLookVec();
		double targetX = seeker.posX;
		double targetY = seeker.posY + seeker.getEyeHeight() - 0.10000000149011612D;
		double targetZ = seeker.posZ;
		double distanceTraveled = 0;

		while ((int) distanceTraveled < distance) {
			targetX += vec3.xCoord;
			targetY += vec3.yCoord;
			targetZ += vec3.zCoord;
			distanceTraveled += vec3.lengthVector();
			List<EntityLivingBase> list = seeker.worldObj.getEntitiesWithinAABB(EntityLivingBase.class,
					AxisAlignedBB.getBoundingBox(targetX-radius, targetY-radius, targetZ-radius, targetX+radius, targetY+radius, targetZ+radius));
			for (EntityLivingBase target : list) {
				if (target != seeker && target.canBeCollidedWith() && isTargetInSight(vec3, seeker, target)) {
					double newDistance = (closestToSeeker ? target.getDistanceSqToEntity(seeker) : target.getDistanceSq(targetX, targetY, targetZ));
					if (newDistance < currentDistance) {
						currentTarget = target;
						currentDistance = newDistance;
					}
				}
			}
		}

		return currentTarget;
	}

	/**
	 * Similar to the single entity version, but this method returns a List of all EntityLivingBase entities
	 * that are within the entity's field of vision, up to a certain range and distance away
	 */
	@SuppressWarnings("unchecked")
	public static final List<EntityLivingBase> acquireAllLookTargets(EntityLivingBase seeker, int distance, double radius) {
		if (distance < 0 || distance > MAX_DISTANCE) {
			distance = MAX_DISTANCE;
		}
		List<EntityLivingBase> targets = new ArrayList<EntityLivingBase>();
		Vec3 vec3 = seeker.getLookVec();
		double targetX = seeker.posX;
		double targetY = seeker.posY + seeker.getEyeHeight() - 0.10000000149011612D;
		double targetZ = seeker.posZ;
		double distanceTraveled = 0;

		while ((int) distanceTraveled < distance) {
			targetX += vec3.xCoord;
			targetY += vec3.yCoord;
			targetZ += vec3.zCoord;
			distanceTraveled += vec3.lengthVector();
			List<EntityLivingBase> list = seeker.worldObj.getEntitiesWithinAABB(EntityLivingBase.class,
					AxisAlignedBB.getBoundingBox(targetX-radius, targetY-radius, targetZ-radius, targetX+radius, targetY+radius, targetZ+radius));
			for (EntityLivingBase target : list) {
				if (target != seeker && target.canBeCollidedWith() && isTargetInSight(vec3, seeker, target)) {
					if (!targets.contains(target)) {
						targets.add(target);
					}
				}
			}
		}

		return targets;
	}

	/**
	 * Returns whether the target is in the seeker's field of view based on relative position
	 * @param fov seeker's field of view; a wider angle returns true more often
	 */
	public static final boolean isTargetInFrontOf(Entity seeker, Entity target, float fov) {
		double dx = target.posX - seeker.posX;
		double dz;
		for (dz = target.posZ - seeker.posZ; dx * dx + dz * dz < 1.0E-4D; dz = (Math.random() - Math.random()) * 0.01D) {
			dx = (Math.random() - Math.random()) * 0.01D;
		}
		float yaw = (float)(Math.atan2(dz, dx) * 180.0D / Math.PI) - seeker.rotationYaw;
		yaw = yaw - 90;
		while (yaw < -180) { yaw += 360; }
		while (yaw >= 180) { yaw -= 360; }
		return yaw < fov && yaw > -fov;
	}

	/**
	 * Returns true if the target's position is within the area that the seeker is facing and the target can be seen
	 */
	public static final boolean isTargetInSight(EntityLivingBase seeker, Entity target) {
		return isTargetInSight(seeker.getLookVec(), seeker, target);
	}

	/**
	 * Returns true if the target's position is within the area that the seeker is facing and the target can be seen
	 */
	private static final boolean isTargetInSight(Vec3 vec3, EntityLivingBase seeker, Entity target) {
		return seeker.canEntityBeSeen(target) && isTargetInFrontOf(seeker, target, 60);
	}

	/**
	 * Returns the chunk coordinates for the entity's current position
	 */
	public static ChunkCoordinates getEntityCoordinates(Entity entity) {
		int i = MathHelper.floor_double(entity.posX + 0.5D);
		int j = MathHelper.floor_double(entity.posY + 0.5D - (entity.worldObj.isRemote ? entity.yOffset : 0D));
		int k = MathHelper.floor_double(entity.posZ + 0.5D);
		return new ChunkCoordinates(i,j,k);
	}

	/**
	 * Whether the entity is currently standing in any liquid
	 */
	public static boolean isInLiquid(Entity entity) {
		ChunkCoordinates cc = getEntityCoordinates(entity);
		Block block = entity.worldObj.getBlock(cc.posX, cc.posY, cc.posZ);
		return block.getMaterial().isLiquid();
	}

	/**
	 * Knocks the pushed entity back slightly as though struck by the pushing entity
	 */
	public static final void knockTargetBack(EntityLivingBase pushedEntity, EntityLivingBase pushingEntity) {
		if (pushedEntity.canBePushed()) {
			double dx = pushedEntity.posX - pushingEntity.posX;
			double dz;
			for (dz = pushedEntity.posZ - pushingEntity.posZ; dx * dx + dz * dz < 1.0E-4D; dz = (Math.random() - Math.random()) * 0.01D){
				dx = (Math.random() - Math.random()) * 0.01D;
			}
			pushedEntity.knockBack(pushingEntity, 0, -dx, -dz);
		}
	}
}
