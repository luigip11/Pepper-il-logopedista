package it.uniba.dib.sysag.pepper4seriousgames;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import it.uniba.dib.sysag.pepper4seriousgames.First_game.TutorialFirstGame;
import it.uniba.dib.sysag.pepper4seriousgames.Fourth_game.TutorialFourthGame;
import it.uniba.dib.sysag.pepper4seriousgames.Second_game.TutorialSecondGame;
import it.uniba.dib.sysag.pepper4seriousgames.Third_game.TutorialThirdGame;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private Integer [] images = {R.drawable.gioco_animali, R.drawable.gioco_intruso, R.drawable.gioco_indovina, R.drawable.gioco_oggetti};

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.viewpager_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.game1);
        imageView.setImageResource(images[position]);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go;

                // Se clicchi sull'immagine vai alla relativa attività
                if (position == 0) {
                    go = new Intent(view.getContext(), TutorialFirstGame.class);
                    view.getContext().startActivity(go);
                } else if (position == 1) {
                    go = new Intent(view.getContext(), TutorialSecondGame.class);
                    view.getContext().startActivity(go);
                }
                else if (position == 2) {
                    go = new Intent(view.getContext(), TutorialThirdGame.class);
                    view.getContext().startActivity(go);
                }
                else if (position == 3) {
                    go = new Intent(view.getContext(), TutorialFourthGame.class);
                    view.getContext().startActivity(go);
                }

            }
        });

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}
