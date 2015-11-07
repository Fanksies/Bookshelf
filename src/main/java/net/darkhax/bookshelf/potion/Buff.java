package net.darkhax.bookshelf.potion;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.darkhax.bookshelf.lib.util.ItemStackUtils;
import net.darkhax.bookshelf.lib.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class Buff {
    
    private String potionName;
    private boolean isBad;
    private int color;
    
    /**
     * A ResourceLocation that points to an individual potion icon to be used when rendering
     * the status HUD. This texture must be 18x. If this is not set, the iconMap will be
     * checked instead.
     */
    public ResourceLocation icon;
    
    /**
     * A ResourceLocation that points to a 144x icon map. The icon map contains 64 18x images
     * to be used when rendering the status HUD. If this is not set, and icon is not set, the
     * iconStack will be checked instead.
     */
    public ResourceLocation iconMap;
    
    /**
     * An ItemStack that is used when rendering the status HUD. The image used is taken from
     * the ItemStack's icon. If this is not set, and icon and iconMap are not set, no texture
     * will be rendered.
     */
    public ItemStack iconStack;
    
    private int statusIconIndex;
    
    /**
     * Constructs a new instance of Buff. This constructor is used for setting the potion to
     * render in the status HUD by using a single 18x texture.
     *
     * @param potionName: The name of the potion
     * @param isBad: Whether or not this potion effect has a negative effect.
     * @param color: An integer representation of an RGB color. Look into Color.getRGB for more
     *            info on this.
     * @param icon: A ResourceLocation that points to a 18x icon texture that represents this
     *            potion.
     */
    public Buff(String potionName, boolean isBad, int color, ResourceLocation icon) {
        
        this.potionName = potionName;
        this.isBad = isBad;
        this.color = color;
        this.icon = icon;
    }
    
    /**
     * Constructs a new instance of Buff. This constructor is used for setting the potion to
     * use a texture map when rendering. This option allows for multiple icons to be stored in
     * the same texture. An example texture map that follows the specifications can be found in
     * assets/bookshelf/textures/inventory/example_potion_map.png, which has all of the index
     * values labeled. Feel free to re-use this map in your own project, and replace sprites as
     * you go.
     *
     * @param potionName: The name of the potion
     * @param isBad: Whether or not this potion effect has a negative effect.
     * @param color: An integer representation of an RGB color. Look into Color.getRGB for more
     *            info on this.
     * @param iconMap: A ResourceLocation that points to a 144x texture map, which contains 64
     *            18x sprites.
     * @param iconIndex: The sprite to use on the map. 0 is the first sprite, and 64 is the
     *            last. The formula used when rendering is X = index % 8 * 18 and Y = index / 8
     *            * 18. The appropriate indexes are also labeled in the example map.
     */
    public Buff(String potionName, boolean isBad, int color, ResourceLocation iconMap, int iconIndex) {
        
        this.potionName = potionName;
        this.isBad = isBad;
        this.color = color;
        this.iconMap = iconMap;
        this.setIconIndex(iconIndex % 8, iconIndex / 8);
    }
    
    /**
     * Constructs a new instance of Buff. This constructor is used for setting an ItemStack to
     * be used when rendering the potion icon in the status HUD.
     *
     * @param potionName: The name of the potion
     * @param isBad: Whether or not this potion effect has a negative effect.
     * @param color: An integer representation of an RGB color. Look into Color.getRGB for more
     *            info on this.
     * @param iconStack: An ItemStack that is used to grab an icon, which will be used when
     *            rendering the potion in the status HUD.
     */
    public Buff(String potionName, boolean isBad, int color, ItemStack iconStack) {
        
        this.potionName = potionName;
        this.isBad = isBad;
        this.color = color;
        this.iconStack = iconStack;
    }
    
    /**
     * Provides access to the basic Potion constructor. This version of the constructor has no
     * support for rendering in the status HUD, and can be used to specify your own method for
     * rendering, or not to render anything at all.
     *
     * @param potionName: The name of the potion
     * @param isBad: Whether or not this potion effect has a negative effect.
     * @param color: An integer representation of an RGB color. Look into Color.getRGB for more
     *            info on this.
     */
    public Buff(String potionName, boolean isBad, int color) {
        
        this.potionName = potionName;
        this.isBad = isBad;
        this.color = color;
    }
    
    protected Buff setIconIndex (int p_76399_1_, int p_76399_2_) {
        
        this.statusIconIndex = p_76399_1_ + p_76399_2_ * 8;
        return this;
    }
    
    public boolean canUpdate () {
        
        return true;
    }
    
    public void onBuffTick (World world, EntityLivingBase entity, int duration, int power) {
    
    }
    
    public boolean shouldRenderInvText (BuffEffect effect) {
        
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    public void renderInventoryEffect (int x, int y, BuffEffect effect, Minecraft mc) {
        
        if (this.icon != null) {
            
            Minecraft.getMinecraft().renderEngine.bindTexture(this.icon);
            RenderUtils.drawTextureModalRectSize(x + 6, y + 7, 0, 0, 18, 18, 18, 1f);
        }
        
        else if (ItemStackUtils.isValidStack(this.iconStack)) {
            
            Minecraft.getMinecraft().renderEngine.bindTexture(Minecraft.getMinecraft().renderEngine.getResourceLocation(1));
            mc.currentScreen.drawTexturedModelRectFromIcon(x + 8, y + 8, this.iconStack.getIconIndex(), 16, 16);
        }
        
        else if (this.iconMap != null) {
            
            int index = this.getStatusIconIndex();
            Minecraft.getMinecraft().renderEngine.bindTexture(this.iconMap);
            RenderUtils.drawTextureModalRectSize(x + 6, y + 7, index % 8 * 18, index / 8 * 18, 18, 18, 144, 1f);
        }
    }
    
    @SideOnly(Side.CLIENT)
    public boolean hasStatusIcon () {
        
        return this.statusIconIndex >= 0;
    }
    
    public int getStatusIconIndex () {
        
        return this.statusIconIndex;
    }
    
    public String getPotionName () {
        
        return potionName;
    }
}