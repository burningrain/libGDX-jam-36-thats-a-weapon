package com.github.br.libgdx.jam36.screens.phase.game;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.github.br.libgdx.jam36.ui.AnimatedImage;

public class Watch {

    private final float DEGREES_PER_HOUR = 360f / 12f; // 30 градусов

    private float secondsPerHour = 20f;
    private float rotationSpeed = DEGREES_PER_HOUR / secondsPerHour;

    private final Image arrow;
    private final AnimatedImage animationWeekDay;

    private WeekDay currentWeekDay = WeekDay.MONDAY;

    public Watch(Image arrow, AnimatedImage animationWeekDay) {
        this.arrow = arrow;
        this.animationWeekDay = animationWeekDay;
        // Убедимся, что точка вращения установлена (если не сделали это снаружи)
        this.arrow.setOrigin(arrow.getWidth() / 2f, 0);
        setWeekDay(currentWeekDay);
    }

    public void setSecondsPerHour(float secondsPerHour) {
        this.secondsPerHour = secondsPerHour;
        this.rotationSpeed = DEGREES_PER_HOUR / secondsPerHour;
    }

    public float getSecondsPerHour() {
        return secondsPerHour;
    }

    private void setWeekDay(WeekDay currentWeekDay) {
        animationWeekDay.setFrameAndPause(currentWeekDay.getDayNumber() - 1);
    }

    public WeekDay getCurrentWeekDay() {
        return currentWeekDay;
    }

    public void update(float deltaTime) {
        // Вращение по часовой стрелке (уменьшаем угол)
        arrow.rotateBy(-(rotationSpeed * deltaTime));
    }

    /**
     * 1) Получить текущее время (0.0 - 12.0) на основе угла стрелки.
     */
    public float getTime() {
        float rotation = arrow.getRotation();

        // Приводим угол к диапазону [0, 360) в положительную сторону для удобства расчета
        // В LibGDX вращение по часовой дает отрицательные числа.
        // % 360f даст остаток, а прибавление 360 гарантирует, что мы не в минусе.
        float normalizedAngle = (-rotation % 360f + 360f) % 360f;

        return normalizedAngle / DEGREES_PER_HOUR;
    }

    /**
     * 2) Установить угол стрелки согласно времени (0 - 12).
     * Пример: 3 часа -> -90 градусов.
     */
    public void setTime(float hours) {
        // Ограничиваем входные данные кругом в 12 часов
        float normalizedHours = hours % 12f;
        // Переводим часы в градусы и инвертируем (так как идем по часовой)
        arrow.setRotation(-(normalizedHours * DEGREES_PER_HOUR));
    }

    public float convertHoursToRealSeconds(float hours) {
        return hours * secondsPerHour;
    }



}
