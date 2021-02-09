![Spruce Logo](https://github.com/willowtreeapps/spruce-android/blob/master/imgs/header_image.png)

# Spruce Android Animation Library (and [iOS](https://github.com/willowtreeapps/spruce-ios))
[![CircleCI Build Status](https://circleci.com/gh/willowtreeapps/spruce-android.svg?style=shield)](https://circleci.com/gh/willowtreeapps/spruce-android)
[![License MIT](https://img.shields.io/badge/License-MIT-blue.svg?style=flat)]()
[![Public Yes](https://img.shields.io/badge/Public-yes-green.svg?style=flat)]()

## What is it?
Spruce is a lightweight animation library that helps choreograph the animations on the screen. With so many different animation libraries out there, developers need to make sure that each view is animating at the appropriate time. Spruce can help designers request complex multi-view animations and not have the developers cringe at the prototype.

<p align="center">
<img src="https://github.com/willowtreeapps/spruce-android/blob/master/imgs/recycler-example.gif" width=25% height=25%/>
</p>

### Gradle
Add the following to your project's root build.gradle file
```gradle
repositories {
	maven {
		url  "https://dl.bintray.com/bfears/maven"
	}
}
```
Add the following to your project's build.gradle file

```gradle
dependencies {
    implementation 'com.willowtreeapps.spruce:spruce-android:1.1.0'
}
```

## Documentation
For javadocs checkout [the documentation](https://willowtreeapps.github.io/spruce-android/) for more information.

## Basic Usage
```java
Animator spruceAnimator = new Spruce
        .SpruceBuilder(parentViewGroup)
        .sortWith(new Default(/*interObjectDelay=*/50L))
        .animateWith(new Animator[] {DefaultAnimations.shrinkAnimator(parentViewGroup, /*duration=*/800)})
        .start();
```

Checkout [the builder documentation](https://willowtreeapps.github.io/spruce-android/com/willowtreeapps/spruce/Spruce.SpruceBuilder.html) for more information.

### Preparing for Animation
Spruce comes packed with `Animator` options within the `DefaultAnimations` class meant to make your life easier when calling an animation. Let's say we want to have your views fade in. For example, we would create an `animators = new Animator[] {}` and add `DefaultAnimations.fadeInAnimator(parentViewGroup, /*duration=*/800)` as an array item.
If you want a view to fade in, then you need to make sure that it is already faded out. To do that, we need to set the alpha to `0` on the views or you could first use a fade out animator.

### Running the Animation 

Use the following command to run a basic animation on your view.

```java
Animator spruceAnimator = new Spruce
        .SpruceBuilder(parentViewGroup)
        .sortWith(new DefaultSort(/*interObjectDelay=*/50L))
        .animateWith(animators)
        .start();
```

Checkout [default animation documentation](https://willowtreeapps.github.io/spruce-android/com/willowtreeapps/spruce/animation/DefaultAnimations.html) for more information.

## Using a SortFunction
Luckily, Spruce comes with 8 `SortFunction` implementations with a wide open possibility to make more! Use the `SortFunction` to change the order in which views animate. Consider the following example:

```java
LinearSort sort = new LinearSort(/*interObjectDelay=*/100L, /*reversed=*/false, LinearSort.Direction.TOP_TO_BOTTOM);
```
In this example we have created a `LinearSort` which will have views animate in from the top to bottom. We can change the look and feel of the animation by using a `RadialSort` instead which will have the views animate in a circular fashion. If we wanted to use this `sort` in an actual Spruce `start()` call then that would look something like:

```java
Animator spruceAnimator = new Spruce
        .SpruceBuilder(parentViewGroup)
        .sortWith(new LinearSort(/*interObjectDelay=*/100L, /*reversed=*/false, LinearSort.Direction.TOP_TO_BOTTOM))
        .animateWith(DefaultAnimations.shrinkAnimator(parentViewGroup, /*duration=*/800))
        .start();
```
Definitely play around with the stock `SortFunction` implementations until you find the one that is perfect for you! Check out the example app if you want to get previews of what each `SortFunction` will look like.

### The Animators
The animations used in Spruce are produced by leveraging the `Animator` class. You may provide your own custom animations by creating your own `Animator` and provide it to the as part of an `Animator[]` to `SpruceBuilder.animateWith(Animator... animators)`. For more information on using the `Animator` class please check out https://developer.android.com/reference/android/animation/Animator.html

### Standard Animation
The `DefaultAnimation` class provides simple `Animator` methods to apply the change `Animator` to the views. Use this class if you want to have a stock linear movement of the changes.

## Sort Functions
With all different types of animations, especially those dealing with subviews, we have to consider a way in which we want to animate them. Some views can have 0 subviews while others may have hundreds. To handle this, we have the notion of a `SortFunction`. What this will do is take each of the subviews in the `ViewGroup`, and apply a mapping from the specific subview to the exact delay that it should wait before animating. Some of these will sort in a radial formation while others may actually sort randomly. One of the cool features of Spruce, is that you can actually define your own `SortFunction` and then the animation will look completely different. Luckily, Spruce also comes jam packed with a ton of default `SortFunction` classes to make everything easier on you as the developer. Take a look at some of the default `SortFunction` classes we have and see if you can use them or branch off of them for your cool and custom animations!

### The SortFunction Interface
A very simple interface that requires classes to extend the following class

```java
public abstract class SortFunction {
    public abstract List<SpruceTimedView> getViewListWithTimeOffsets(ViewGroup parent, List<View> children);
}
```

What the above class needs to do is take in a `ViewGroup` parent and a `List` of `View` children or subviews to generate a list of subviews and their animation offsets. Once the list of subviews has been generated, you can define your own sort metric to determine in which order the `View`'s should animate. To do so, you need to create a `List` of `SpruceTimedView`'s. This special class has two properties: (1) `View view` and (2) `long timeOffset`. Your `SortFunction` can define the `timeOffset` however it likes, but the animators will use this to determine how long it should delay the start of that specific view from animating. The best way to learn, is to play around. So why not have some fun and make your own `SortFunction`!

### About Sort Functions
To make sure that developers can use Spruce out of the box, we included about 8 stock `SortFunction` implementations. These are some of the main functions we use at WillowTree and are so excited to see what others come up with!

- `DefaultSort`
- `LinearSort`
- `CorneredSort`
- `RadialSort`
- `RandomSort`
- `InlineSort`
- `ContinousSort`
- `ContinuousWeightedSort`

Check out the docs [here](https://willowtreeapps.github.io/spruce-android/com/willowtreeapps/spruce/sort/SortFunction.html) for more information

### View Exclusion Feature

Spruce Animate all the views inside the view group. One of the key tips for pulling the best performance out of an Android app is to maintain a flat hierarchy. Spruce is now Introducing a new Exclusion feature.  
This work in 2 modes:
- NORMAL_MODE: This mode should be used when you have view groups like Constraint/Frame/Relative/Linear Layouts. We feed a list of ids to be excluded to the SpruceBuilder.
- R_L_MODE: This mode is used when we have ListView/RecyclerView. The only difference with the first mode is that we pass in the positions to be excluded instead of Ids.

```java
Animator spruceAnimator = new Spruce
        .SpruceBuilder(parentViewGroup)
        .sortWith(new LinearSort(/*interObjectDelay=*/100L, /*reversed=*/false, LinearSort.Direction.TOP_TO_BOTTOM))
        .excludeViews(getExcludedViewIds(), NORMAL_MODE)
        //or 
       .excludeViews(getExcludedViewPosition(), R_L_MODE)
        .start();
```

### Sort Function Interpolators

Spruce now allows the user to control the overall flow of sort function using Interpolators. 

```java
Animator spruceAnimator = new Spruce
        .SpruceBuilder(parentViewGroup)
        .sortWith(new LinearSort(/*interObjectDelay=*/100L, /*reversed=*/false, LinearSort.Direction.TOP_TO_BOTTOM))
        .addInterpolator(new LinearInterpolator())
        .start();
```

Spruce gives you a wide variety of stock interpolators to choose from.

- `SpruceInterpolators.EASE`
- `SpruceInterpolators.EASE_IN`
- `SpruceInterpolators.EASE_OUT`
- `SpruceInterpolators.EASE_IN_OUT`
- `SpruceInterpolators.EASE_IN_QUAD`
- `SpruceInterpolators.EASE_IN_CUBIC`
- `SpruceInterpolators.EASE_IN_QUART`
- `SpruceInterpolators.EASE_IN_QUINT`
- `SpruceInterpolators.EASE_IN_SINE`
- `SpruceInterpolators.EASE_IN_EXPO`
- ` SpruceInterpolators.EASE_IN_CIRC`
- ` SpruceInterpolators.EASE_IN_BACK`
- ` SpruceInterpolators.EASE_OUT_QUAD`
- ` SpruceInterpolators.EASE_OUT_CUBIC`
- ` SpruceInterpolators.EASE_OUT_QUART`
- ` SpruceInterpolators.EASE_OUT_QUINT`
- ` SpruceInterpolators.EASE_OUT_SINE`
- ` SpruceInterpolators.EASE_OUT_EXPO`
- ` SpruceInterpolators.EASE_OUT_CIRC`
- ` SpruceInterpolators.EASE_OUT_BACK`
- ` SpruceInterpolators.EASE_IN_OUT_QUAD`
- ` SpruceInterpolators.EASE_IN_OUT_CUBIC`
- ` SpruceInterpolators.EASE_IN_OUT_QUART`
- ` SpruceInterpolators.EASE_IN_OUT_QUINT`
- ` SpruceInterpolators.EASE_IN_OUT_SINE`
- ` SpruceInterpolators.EASE_IN_OUT_EXPO`
- ` SpruceInterpolators.EASE_IN_OUT_CIRC`
- ` SpruceInterpolators.EASE_IN_OUT_BACK` 

Checkout [interpolator documentation](https://developer.android.com/reference/android/view/animation/Interpolator) for more information.

## Spruce Dynamics

Spruce now supports Dynamic Animations. Spruce Dynamics is an extension of the [androidx dynamic animations](https://developer.android.com/jetpack/androidx/releases/dynamicanimation).

These are the option that SpruceDynamics exposes to the developers:
- Allows start delay for dynamic animations
- Animation Property is now exposed (developers can set progress of the animations dynamically) 


You can create your own Spring/Fling animations from SpruceDynamics and add them to the '.animateWith' function for
playing the animations in the respective ViewGroup

```java
Animator spruceAnimator = new Spruce
        .SpruceBuilder(parentViewGroup)
        .sortWith(new LinearSort(/*interObjectDelay=*/100L, /*reversed=*/false, LinearSort.Direction.TOP_TO_BOTTOM))
        .animateWith(DefaultAnimations.dynamicTranslationUpwards(parent))
        .start();
```

Above all these, With spruce, you can implement a combination of both Android Animations and Spruce Dynamics at the same time.

 ```java

 animators = new Object[]{
         DefaultAnimations.dynamicTranslationUpwards(parent),
         DefaultAnimations.dynamicFadeIn(parent),
         DefaultAnimations.shrinkAnimator(parent,800)
 };


 Animator spruceAnimator = new Spruce
         .SpruceBuilder(parentViewGroup)
         .sortWith(new LinearSort(/*interObjectDelay=*/100L, /*reversed=*/false, LinearSort.Direction.TOP_TO_BOTTOM))
         .animateWith(animators)
         .start();
 ```

## Stock Animators
To make everybody's lives easier, the stock animators perform basic `View` animations that a lot of apps use today. Mix and match these animators to get the core motion you are looking for.

- `DefaultAnimations.growAnimator(View view, long duration)`
- `DefaultAnimations.shrinkAnimator(View view, long duration)`
- `DefaultAnimations.fadeAwayAnimator(View view, long duration)`
- `DefaultAnimations.fadeInAnimator(View view, long duration)`
- `DefaultAnimations.spinAnimator(View view, long duration)`
- `DefaultAnimations.dynamicTranslationUpwards(View view)`
- `DefaultAnimations.dynamicFadeIn(View view, long duration)`

Experiment which ones work for you! If you think of anymore feel free to add them to the library yourself!

# Example App
Use the [example app](https://github.com/willowtreeapps/spruce-android/tree/master/app) to find the right `SortFunction`. In the app you will be able to see the affects of each `SortFunction`.

## Contributing to Spruce
Contributions are more than welcome! Please see the [Contributing Guidelines](https://github.com/willowtreeapps/spruce-android/blob/master/Contributing.md) and be mindful of our [Code of Conduct](https://github.com/willowtreeapps/spruce-android/blob/master/code-of-conduct.md).

# Issues or Future Ideas
If part of Spruce is not working correctly be sure to file a Github issue. In the issue provide as many details as possible. This could include example code or the exact steps that you did so that everyone can reproduce the issue. Sample projects are always the best way :). This makes it easy for our developers or someone from the open-source community to start working!

If you have a feature idea submit an issue with a feature request or submit a pull request and we will work with you to merge it in!

## Third Party Bindings

### React Native
You may now use this library with [React Native](https://github.com/facebook/react-native) via the module [here](https://github.com/prscX/react-native-spruce)


# About WillowTree!
![WillowTree Logo](https://github.com/willowtreeapps/spruce-android/blob/master/imgs/willowtree_logo.png)

We build apps, responsive sites, bots—any digital product that lives on a screen—for the world’s leading companies. Our elite teams challenge themselves to build extraordinary experiences by bridging the latest strategy and design thinking with enterprise-grade software development.

Interested in working on more unique projects like Spruce? Check out our [careers page](http://willowtreeapps.com/careers?utm_campaign=spruce-gh).

