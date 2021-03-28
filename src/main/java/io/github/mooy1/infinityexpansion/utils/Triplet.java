package io.github.mooy1.infinityexpansion.utils;

import lombok.Data;
import lombok.NoArgsConstructor;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

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
		// TODO Auto-generated method stub
		return a;
	}
	public String getB() {
		// TODO Auto-generated method stub
		return b;
	}
	public int getC() {
		// TODO Auto-generated method stub
		return c;
	}
    
}
