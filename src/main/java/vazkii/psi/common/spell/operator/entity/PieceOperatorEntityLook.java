/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Psi Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Psi
 *
 * Psi is Open Source and distributed under the
 * Psi License: http://psi.vazkii.us/license.php
 *
 * File Created @ [18/01/2016, 22:01:07 (GMT)]
 */
package vazkii.psi.common.spell.operator.entity;

import net.minecraft.entity.Entity;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorEntityLook extends PieceOperator {

	SpellParam<Entity> target;

    public PieceOperatorEntityLook(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(target = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
    }

    //Projectiles and falling blocks and items should have their look be the motion TODO
    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Entity e = this.getParamValue(context, target);

        if (e == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);

        return new Vector3(e.getLook(1F));
    }

	@Override
	public Class<?> getEvaluationType() {
		return Vector3.class;
	}

}
