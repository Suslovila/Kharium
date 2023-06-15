package com.suslovila.utils;



public class SUSVec3 {
   public static final SUSVec3 ZERO = new SUSVec3(0.0D, 0.0D, 0.0D);
   public final double x;
   public final double y;
   public final double z;

   public static SUSVec3 fromRGB24(int p_82502_) {
      double d0 = (double)(p_82502_ >> 16 & 255) / 255.0D;
      double d1 = (double)(p_82502_ >> 8 & 255) / 255.0D;
      double d2 = (double)(p_82502_ & 255) / 255.0D;
      return new SUSVec3(d0, d1, d2);
   }
//
//   public static Vec3 atCenterOf(Vec3 p_82513_) {
//      return new Vec3((double)p_82513_.getX() + 0.5D, (double)p_82513_.getY() + 0.5D, (double)p_82513_.getZ() + 0.5D);
//   }
//
//   public static Vec3 atLowerCornerOf(Vec3i p_82529_) {
//      return new Vec3((double)p_82529_.getX(), (double)p_82529_.getY(), (double)p_82529_.getZ());
//   }
//
//   public static Vec3 atBottomCenterOf(Vec3i p_82540_) {
//      return new Vec3((double)p_82540_.getX() + 0.5D, (double)p_82540_.getY(), (double)p_82540_.getZ() + 0.5D);
//   }
//
//   public static Vec3 upFromBottomCenterOf(Vec3i p_82515_, double p_82516_) {
//      return new Vec3((double)p_82515_.getX() + 0.5D, (double)p_82515_.getY() + p_82516_, (double)p_82515_.getZ() + 0.5D);
//   }

   public SUSVec3(double p_82484_, double p_82485_, double p_82486_) {
      this.x = p_82484_;
      this.y = p_82485_;
      this.z = p_82486_;
   }

//   public Vec3(Vector3f p_82488_) {
//      this((double)p_82488_.x(), (double)p_82488_.y(), (double)p_82488_.z());
//   }

   public SUSVec3 vectorTo(SUSVec3 p_82506_) {
      return new SUSVec3(p_82506_.x - this.x, p_82506_.y - this.y, p_82506_.z - this.z);
   }

   public SUSVec3 normalize() {
      double d0 = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
      return d0 < 1.0E-4D ? ZERO : new SUSVec3(this.x / d0, this.y / d0, this.z / d0);
   }

   public double dot(SUSVec3 p_82527_) {
      return this.x * p_82527_.x + this.y * p_82527_.y + this.z * p_82527_.z;
   }

   public SUSVec3 cross(SUSVec3 p_82538_) {
      return new SUSVec3(this.y * p_82538_.z - this.z * p_82538_.y, this.z * p_82538_.x - this.x * p_82538_.z, this.x * p_82538_.y - this.y * p_82538_.x);
   }

   public SUSVec3 subtract(SUSVec3 p_82547_) {
      return this.subtract(p_82547_.x, p_82547_.y, p_82547_.z);
   }

   public SUSVec3 subtract(double p_82493_, double p_82494_, double p_82495_) {
      return this.add(-p_82493_, -p_82494_, -p_82495_);
   }

   public SUSVec3 add(SUSVec3 p_82550_) {
      return this.add(p_82550_.x, p_82550_.y, p_82550_.z);
   }

   public SUSVec3 add(double p_82521_, double p_82522_, double p_82523_) {
      return new SUSVec3(this.x + p_82521_, this.y + p_82522_, this.z + p_82523_);
   }

//   public boolean closerThan(Position p_82510_, double p_82511_) {
//      return this.distanceToSqr(p_82510_.x(), p_82510_.y(), p_82510_.z()) < p_82511_ * p_82511_;
//   }

   public double distanceTo(SUSVec3 p_82555_) {
      double d0 = p_82555_.x - this.x;
      double d1 = p_82555_.y - this.y;
      double d2 = p_82555_.z - this.z;
      return Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
   }

   public double distanceToSqr(SUSVec3 p_82558_) {
      double d0 = p_82558_.x - this.x;
      double d1 = p_82558_.y - this.y;
      double d2 = p_82558_.z - this.z;
      return d0 * d0 + d1 * d1 + d2 * d2;
   }

   public double distanceToSqr(double p_82532_, double p_82533_, double p_82534_) {
      double d0 = p_82532_ - this.x;
      double d1 = p_82533_ - this.y;
      double d2 = p_82534_ - this.z;
      return d0 * d0 + d1 * d1 + d2 * d2;
   }

   public SUSVec3 scale(double p_82491_) {
      return this.multiply(p_82491_, p_82491_, p_82491_);
   }

   public SUSVec3 reverse() {
      return this.scale(-1.0D);
   }

   public SUSVec3 multiply(SUSVec3 p_82560_) {
      return this.multiply(p_82560_.x, p_82560_.y, p_82560_.z);
   }

   public SUSVec3 multiply(double p_82543_, double p_82544_, double p_82545_) {
      return new SUSVec3(this.x * p_82543_, this.y * p_82544_, this.z * p_82545_);
   }

   public double length() {
      return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
   }

   public double lengthSqr() {
      return this.x * this.x + this.y * this.y + this.z * this.z;
   }

   public double horizontalDistance() {
      return Math.sqrt(this.x * this.x + this.z * this.z);
   }

   public double horizontalDistanceSqr() {
      return this.x * this.x + this.z * this.z;
   }

   public boolean equals(Object p_82552_) {
      if (this == p_82552_) {
         return true;
      } else if (!(p_82552_ instanceof SUSVec3)) {
         return false;
      } else {
         SUSVec3 SUSVec3 = (SUSVec3)p_82552_;
         if (Double.compare(SUSVec3.x, this.x) != 0) {
            return false;
         } else if (Double.compare(SUSVec3.y, this.y) != 0) {
            return false;
         } else {
            return Double.compare(SUSVec3.z, this.z) == 0;
         }
      }
   }

   public int hashCode() {
      long j = Double.doubleToLongBits(this.x);
      int i = (int)(j ^ j >>> 32);
      j = Double.doubleToLongBits(this.y);
      i = 31 * i + (int)(j ^ j >>> 32);
      j = Double.doubleToLongBits(this.z);
      return 31 * i + (int)(j ^ j >>> 32);
   }

   public String toString() {
      return "(" + this.x + ", " + this.y + ", " + this.z + ")";
   }

   public SUSVec3 lerp(SUSVec3 p_165922_, double p_165923_) {
      return new SUSVec3(lerp(p_165923_, this.x, p_165922_.x), lerp(p_165923_, this.y, p_165922_.y), lerp(p_165923_, this.z, p_165922_.z));
   }
   public static double lerp(double p_14140_, double p_14141_, double p_14142_) {
      return p_14141_ + p_14140_ * (p_14142_ - p_14141_);
   }
   public SUSVec3 xRot(float p_82497_) {
      double f = Math.cos(p_82497_);
      double f1 = Math.sin(p_82497_);
      double d0 = this.x;
      double d1 = this.y * (double)f + this.z * (double)f1;
      double d2 = this.z * (double)f - this.y * (double)f1;
      return new SUSVec3(d0, d1, d2);
   }

   public SUSVec3 yRot(float p_82525_) {
      double f = Math.cos(p_82525_);
      double f1 = Math.sin(p_82525_);
      double d0 = this.x * (double)f + this.z * (double)f1;
      double d1 = this.y;
      double d2 = this.z * (double)f - this.x * (double)f1;
      return new SUSVec3(d0, d1, d2);
   }

   public SUSVec3 zRot(float p_82536_) {
      float f = (float)Math.cos(p_82536_);
      float f1 = (float)Math.sin(p_82536_);
      double d0 = this.x * (double)f + this.y * (double)f1;
      double d1 = this.y * (double)f - this.x * (double)f1;
      double d2 = this.z;
      return new SUSVec3(d0, d1, d2);
   }

//   public static Vec3 directionFromRotation(Vec2 p_82504_) {
//      return directionFromRotation(p_82504_.x, p_82504_.y);
//   }

   public static SUSVec3 directionFromRotation(float p_82499_, float p_82500_) {
      double f = Math.cos(-p_82500_ * ((float)Math.PI / 180F) - (float)Math.PI);
      double f1 = Math.sin(-p_82500_ * ((float)Math.PI / 180F) - (float)Math.PI);
      double f2 = -Math.cos(-p_82499_ * ((float)Math.PI / 180F));
      double f3 = Math.sin(-p_82499_ * ((float)Math.PI / 180F));
      return new SUSVec3((double)(f1 * f2), (double)f3, (double)(f * f2));
   }

//   public Vec3 align(EnumSet<Direction.Axis> p_82518_) {
//      double d0 = p_82518_.contains(Direction.Axis.X) ? (double)Mth.floor(this.x) : this.x;
//      double d1 = p_82518_.contains(Direction.Axis.Y) ? (double)Mth.floor(this.y) : this.y;
//      double d2 = p_82518_.contains(Direction.Axis.Z) ? (double)Mth.floor(this.z) : this.z;
//      return new Vec3(d0, d1, d2);
//   }

//   public double get(Direction.Axis p_82508_) {
//      return p_82508_.choose(this.x, this.y, this.z);
//   }
//
//   public Vec3 with(Direction.Axis p_193104_, double p_193105_) {
//      double d0 = p_193104_ == Direction.Axis.X ? p_193105_ : this.x;
//      double d1 = p_193104_ == Direction.Axis.Y ? p_193105_ : this.y;
//      double d2 = p_193104_ == Direction.Axis.Z ? p_193105_ : this.z;
//      return new Vec3(d0, d1, d2);
//   }

   public final double x() {
      return this.x;
   }

   public final double y() {
      return this.y;
   }

   public final double z() {
      return this.z;
   }
}