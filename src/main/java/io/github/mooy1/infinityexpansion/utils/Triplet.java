package io.github.mooy1.infinityexpansion.utils;

import lombok.Data;
import lombok.NoArgsConstructor;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;

@Data
@NoArgsConstructor
public final class Triplet<A, B, C> {

    private SlimefunItemStack a;
    private String b;
    private int c;
    public Triplet(SlimefunItemStack item, String id, int i) {
		this.a = item;
		this.b = id;
		this.c = i;
	}
	public SlimefunItemStack getA() {
		return a;
	}
	public String getB() {
		return b;
	}
	public int getC() {
		return c;
	}
    
}
