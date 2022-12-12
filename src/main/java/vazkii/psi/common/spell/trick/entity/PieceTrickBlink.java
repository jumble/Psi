/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [29/01/2016, 17:04:45 (GMT)]
 */
package vazkii.psi.common.spell.trick.entity;

import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import vazkii.arl.network.NetworkHandler;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.network.message.MessageBlink;

public class PieceTrickBlink extends PieceTrick {

	SpellParam target;
	SpellParam distance;

	public PieceTrickBlink(Spell spell) {
		super(spell);
	}

	@Override
	public void initParams() {
		addParam(target = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
		addParam(distance = new ParamNumber(SpellParam.GENERIC_NAME_DISTANCE, SpellParam.RED, false, true));
	}

	@Override
	public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
		super.addToMetadata(meta);
		Double distanceVal = this.<Double>getParamEvaluation(distance);
		if(distanceVal == null)
			distanceVal = 1D;

		meta.addStat(EnumSpellStat.POTENCY, (int) (Math.abs(distanceVal) * 30));
		meta.addStat(EnumSpellStat.COST, (int) (Math.abs(distanceVal) * 40));
	}

	@Override
	public Object execute(SpellContext context) throws SpellRuntimeException {
		Entity targetVal = this.getParamValue(context, target);
		Double distanceVal = this.<Double>getParamValue(context, distance);

		blink(context, targetVal, distanceVal);

		return null;
	}

	public static void blink(SpellContext context, Entity e, double dist) throws SpellRuntimeException {
		context.verifyEntity(e);
		if(!context.isInRadius(e))
			throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

		Vec3d look = e.getLookVec();

		double offX = look.x * dist;
		double offY = Math.max(0, look.y * dist);
		double offZ = look.z * dist;

		//Prevent blinking when under ender inhibition
		if(e instanceof EntityPlayerMP){
			EntityPlayerMP player = (EntityPlayerMP) e;
			if(player.isPotionActive(Potion.getPotionFromResourceLocation("witchery:ender_inhibition")))
				throw new SpellRuntimeException(SpellRuntimeException.BOSS_IMMUNE);
		}
		
		e.setPosition(e.posX + offX, e.posY + offY, e.posZ + offZ);
		if (e instanceof EntityPlayerMP)
			NetworkHandler.INSTANCE.sendTo(new MessageBlink(offX, offY, offZ), (EntityPlayerMP) e);
	}

}
