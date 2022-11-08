/*
 * BluSunrize
 * Copyright (c) 2020
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 *
 */

package com.tzaranthony.spellbook.client.screens.renders;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.tzaranthony.spellbook.SpellBook;
import net.minecraft.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderType.CompositeState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.OptionalDouble;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.mojang.blaze3d.vertex.DefaultVertexFormat.*;
import static net.minecraft.client.renderer.texture.TextureAtlas.LOCATION_PARTICLES;

//This extends RenderStateShard to get access to various protected members
public class IERenderTypes extends RenderStateShard
{
	public static final VertexFormat BLOCK_WITH_OVERLAY = new VertexFormat(
			ImmutableMap.<String, VertexFormatElement>builder()
					.put("Position", ELEMENT_POSITION)
					.put("Color", ELEMENT_COLOR)
					.put("UV0", ELEMENT_UV0)
					.put("UV1", ELEMENT_UV1)
					.put("UV2", ELEMENT_UV2)
					.put("Normal", ELEMENT_NORMAL)
					.put("Padding", ELEMENT_PADDING)
					.build()
	);
	public static final RenderType LINES;
	public static final RenderType TRANSLUCENT_TRIANGLES;
	public static final RenderType TRANSLUCENT_POSITION_COLOR;
	public static final RenderType TRANSLUCENT_NO_DEPTH;
	public static final RenderType CHUNK_MARKER;
	public static final RenderType POSITION_COLOR_LIGHTMAP;
	public static final RenderType ITEM_DAMAGE_BAR;
	public static final RenderType PARTICLES;
	private static final Function<ResourceLocation, RenderType> GUI_CUTOUT;
	private static final Function<ResourceLocation, RenderType> GUI_TRANSLUCENT;
	private static final ShaderStateShard RENDERTYPE_POSITION_COLOR = RENDERTYPE_LIGHTNING_SHADER;
	protected static final TextureStateShard BLOCK_SHEET_MIPPED = new TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, true);
	protected static final LightmapStateShard LIGHTMAP_DISABLED = new LightmapStateShard(false);
	protected static final TransparencyStateShard TRANSLUCENT_TRANSPARENCY = new TransparencyStateShard("translucent_transparency", () -> {
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
	}, RenderSystem::disableBlend);
	protected static final TransparencyStateShard NO_TRANSPARENCY = new TransparencyStateShard(
			"no_transparency",
			RenderSystem::disableBlend, () -> {
	});
	protected static final DepthTestStateShard DEPTH_ALWAYS = new DepthTestStateShard("always", GL11.GL_ALWAYS);

	static
	{
		CompositeState translucentNoDepthState = CompositeState.builder()
				.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
				.setLineState(new LineStateShard(OptionalDouble.of(2)))
				.setDepthTestState(DEPTH_ALWAYS)
				.setShaderState(RENDERTYPE_POSITION_COLOR)
				.createCompositeState(false);
		LINES = createDefault(
				SpellBook.MOD_ID+":translucent_lines", DefaultVertexFormat.POSITION_COLOR_NORMAL, Mode.LINES,
				CompositeState.builder()
						.setShaderState(RENDERTYPE_LINES_SHADER)
						.setLineState(new LineStateShard(OptionalDouble.of(2)))
						.setLayeringState(VIEW_OFFSET_Z_LAYERING)
						.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
						.setOutputState(ITEM_ENTITY_TARGET)
						.setWriteMaskState(COLOR_DEPTH_WRITE)
						.setCullState(NO_CULL)
						.createCompositeState(false)
		);
		TRANSLUCENT_TRIANGLES = createDefault(
				SpellBook.MOD_ID+":translucent_triangles", DefaultVertexFormat.POSITION_COLOR, Mode.TRIANGLES, translucentNoDepthState
		);
		CompositeState translucentNoTextureState = CompositeState.builder()
				.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
				.setTextureState(BLOCK_SHEET_MIPPED)
				.setShaderState(RENDERTYPE_POSITION_COLOR)
				.createCompositeState(false);
		TRANSLUCENT_POSITION_COLOR = createDefault(
				SpellBook.MOD_ID+":translucent_pos_color", DefaultVertexFormat.POSITION_COLOR, Mode.QUADS, translucentNoTextureState
		);
		TRANSLUCENT_NO_DEPTH = createDefault(
				SpellBook.MOD_ID+":translucent_no_depth", DefaultVertexFormat.POSITION_COLOR, Mode.QUADS, translucentNoDepthState
		);
		CHUNK_MARKER = createDefault(
				SpellBook.MOD_ID+":chunk_marker",
				DefaultVertexFormat.POSITION_COLOR_NORMAL,
				//TODO figure out glitchyness
				Mode.LINES,
				CompositeState.builder()
						.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
						.setShaderState(RENDERTYPE_LINES_SHADER)
						.setLineState(new LineStateShard(OptionalDouble.of(5)))
						.setLayeringState(VIEW_OFFSET_Z_LAYERING)
						.setCullState(NO_CULL)
						.setOutputState(ITEM_ENTITY_TARGET)
						.setWriteMaskState(COLOR_DEPTH_WRITE)
						.createCompositeState(false)
		);
		POSITION_COLOR_LIGHTMAP = createDefault(
				SpellBook.MOD_ID+":pos_color_lightmap",
				DefaultVertexFormat.POSITION_COLOR_LIGHTMAP,
				Mode.QUADS,
				CompositeState.builder()
						.setLightmapState(new LightmapStateShard(true))
						.setShaderState(POSITION_COLOR_LIGHTMAP_SHADER)
						.createCompositeState(false)
		);
		ITEM_DAMAGE_BAR = createDefault(
				SpellBook.MOD_ID+":item_damage_bar",
				DefaultVertexFormat.POSITION_COLOR,
				Mode.QUADS,
				CompositeState.builder()
						.setDepthTestState(DEPTH_ALWAYS)
						.setTextureState(BLOCK_SHEET_MIPPED)
						.setShaderState(POSITION_COLOR_SHADER)
						.setTransparencyState(NO_TRANSPARENCY)
						.createCompositeState(false)
		);
		PARTICLES = createDefault(
				SpellBook.MOD_ID+":particles",
				DefaultVertexFormat.PARTICLE,
				Mode.QUADS,
				CompositeState.builder()
						.setTextureState(new TextureStateShard(LOCATION_PARTICLES, false, false))
						.setShaderState(new ShaderStateShard(GameRenderer::getParticleShader))
						.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
						.setOutputState(TRANSLUCENT_TARGET)
						.setLightmapState(NO_LIGHTMAP)
						.createCompositeState(true)
		);
		GUI_CUTOUT = Util.memoize(texture -> createDefault(
				"gui_"+texture,
				DefaultVertexFormat.POSITION_COLOR_TEX,
				Mode.QUADS,
				makeGuiState(texture).createCompositeState(false)
		));
		GUI_TRANSLUCENT = Util.memoize(texture -> createDefault(
				"gui_translucent_"+texture,
				DefaultVertexFormat.POSITION_COLOR_TEX,
				Mode.QUADS,
				makeGuiState(texture)
						.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
						.createCompositeState(false)
		));
	}

	private IERenderTypes(String p_110161_, Runnable p_110162_, Runnable p_110163_)
	{
		super(p_110161_, p_110162_, p_110163_);
	}

	public static RenderType getGui(ResourceLocation texture)
	{
		return GUI_CUTOUT.apply(texture);
	}

	public static RenderType getGuiTranslucent(ResourceLocation texture)
	{
		return GUI_TRANSLUCENT.apply(texture);
	}

	private static CompositeState.CompositeStateBuilder makeGuiState(ResourceLocation texture)
	{
		return CompositeState.builder()
				.setTextureState(new TextureStateShard(texture, false, false))
				.setShaderState(POSITION_COLOR_TEX_SHADER);
	}

	public static RenderType getLines(float lineWidth)
	{
		//TODO fix all usages with normals
		return createDefault(
				"lines_color_pos_"+lineWidth,
				DefaultVertexFormat.POSITION_COLOR_NORMAL,
				Mode.LINES,
				CompositeState.builder()
						.setLineState(new LineStateShard(OptionalDouble.of(lineWidth)))
						.setShaderState(RENDERTYPE_LINES_SHADER)
						.createCompositeState(false)
		);
	}

	public static RenderType getPositionTex(ResourceLocation texture)
	{
		return createDefault(
				SpellBook.MOD_ID+":pos_tex_"+texture,
				DefaultVertexFormat.POSITION_TEX,
				Mode.QUADS,
				CompositeState.builder()
						.setTextureState(new TextureStateShard(texture, false, false))
						.setShaderState(POSITION_TEX_SHADER)
						.createCompositeState(false)
		);
	}

	private static RenderType createDefault(String name, VertexFormat format, Mode mode, CompositeState state)
	{
		return RenderType.create(name, format, mode, 256, false, false, state);
	}

	public static MultiBufferSource wrapWithStencil(MultiBufferSource in, Consumer<VertexConsumer> setupStencilArea, String name, int ref)
	{
		return wrapWithAdditional(
				in,
				"stencil_"+name+"_"+ref,
				() -> {
					GL11.glEnable(GL11.GL_STENCIL_TEST);
					RenderSystem.colorMask(false, false, false, false);
					RenderSystem.depthMask(false);
					GL11.glStencilFunc(GL11.GL_NEVER, 1, 0xFF);
					GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_KEEP, GL11.GL_KEEP);

					GL11.glStencilMask(0xFF);
					RenderSystem.clear(GL11.GL_STENCIL_BUFFER_BIT, true);
					RenderSystem.disableTexture();
					Tesselator tes = Tesselator.getInstance();
					BufferBuilder bb = tes.getBuilder();
					bb.begin(Mode.QUADS, DefaultVertexFormat.POSITION);
					setupStencilArea.accept(bb);
					tes.end();
					RenderSystem.enableTexture();
					RenderSystem.colorMask(true, true, true, true);
					RenderSystem.depthMask(true);
					GL11.glStencilMask(0x00);
					GL11.glStencilFunc(GL11.GL_EQUAL, ref, 0xFF);
				},
				() -> GL11.glDisable(GL11.GL_STENCIL_TEST)
		);
	}

	private static MultiBufferSource wrapWithAdditional(
			MultiBufferSource in,
			String name,
			Runnable setup,
			Runnable teardown
	)
	{
		return type -> in.getBuffer(new RenderType(
				SpellBook.MOD_ID+":"+type+"_"+name,
				type.format(),
				type.mode(),
				type.bufferSize(),
				type.affectsCrumbling(),
				false, // needsSorting is private and shouldn't be too relevant here
				() -> {
					type.setupRenderState();
					setup.run();
				},
				() -> {
					teardown.run();
					type.clearRenderState();
				}
		)
		{
		});
	}
}