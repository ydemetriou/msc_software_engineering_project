"use client";

import { useState, useEffect } from "react";
import Link from "next/link";
import API_BASE_URL from "@/config/api";

export default function RunRecipe({ params: paramsPromise }) {
  const [id, setId] = useState(null);
  useEffect(() => {
    if (paramsPromise instanceof Promise)
      paramsPromise.then((p) => setId(p.id));
    else setId(paramsPromise.id);
  }, [paramsPromise]);

  const [recipe, setRecipe] = useState(null);
  const [currentStepIndex, setCurrentStepIndex] = useState(0);
  const [loading, setLoading] = useState(true);
  const [isFinished, setIsFinished] = useState(false); // ΝΕΟ State για την ολοκλήρωση

  useEffect(() => {
    if (!id) return;
    fetch(`${API_BASE_URL}/api/recipes/${id}`)
      .then((res) => res.json())
      .then((data) => {
        setRecipe(data);
        setLoading(false);
      })
      .catch((err) => console.error(err));
  }, [id]);

  if (loading || !recipe)
    return (
      <div className="p-10 text-center text-xl">Ετοιμασία κουζίνας...</div>
    );

  const steps = recipe.steps || [];
  const currentStep = steps[currentStepIndex];

  // --- ΥΠΟΛΟΓΙΣΜΟΣ ΠΡΟΟΔΟΥ ---
  const totalRecipeTime = recipe.totalTime || 1;

  // Χρόνος ολοκληρωμένων (προηγούμενων) βημάτων
  let completedTime = steps
    .slice(0, currentStepIndex)
    .reduce((sum, step) => sum + (step.duration || 0), 0);

  // Αν τελειώσαμε, η πρόοδος είναι 100%, αλλιώς υπολογίζουμε
  const progressPercentage = isFinished
    ? 100
    : Math.min(100, Math.round((completedTime / totalRecipeTime) * 100));
  // ---------------------------

  const handleNext = () => {
    if (currentStepIndex < steps.length - 1) {
      setCurrentStepIndex((prev) => prev + 1);
    } else {
      // Είμαστε στο τελευταίο βήμα και πατήσαμε Ολοκλήρωση
      setIsFinished(true);
    }
  };

  // Οθόνη Επιτυχίας
  if (isFinished) {
    return (
      <div className="min-h-screen bg-gray-900 text-white flex flex-col">
        {/* Top Bar με 100% Progress */}
        <div className="bg-gray-800 p-4 border-b border-gray-700">
          <div className="w-full bg-gray-700 h-4 rounded-full overflow-hidden relative">
            <div className="bg-green-500 h-full w-full"></div>
            <span className="absolute inset-0 flex items-center justify-center text-xs font-bold text-white">
              100%
            </span>
          </div>
        </div>

        <div className="flex-1 flex flex-col items-center justify-center p-8 text-center">
          <div className="text-8xl mb-6">🎉</div>
          <h1 className="text-4xl font-bold text-green-400 mb-4">Μπράβο!</h1>
          <p className="text-2xl text-gray-300 mb-8">
            Ολοκλήρωσες τη συνταγή <strong>{recipe.name}</strong>!
          </p>
          <Link href="/">
            <button className="bg-green-600 text-white px-8 py-3 rounded-full text-lg shadow-lg hover:bg-green-700 font-bold">
              Επιστροφή στο Μενού
            </button>
          </Link>
        </div>
      </div>
    );
  }

  // Κανονική Οθόνη Βήματος
  return (
    <div className="min-h-screen bg-gray-900 text-white flex flex-col">
      {/* Top Bar */}
      <div className="bg-gray-800 p-4 border-b border-gray-700 sticky top-0 z-10">
        <div className="flex justify-between items-center mb-2">
          <h2 className="text-lg font-bold text-gray-300 truncate">
            {recipe.name}
          </h2>
          <Link
            href={`/recipe/${id}`}
            className="text-sm text-gray-400 hover:text-white"
          >
            ✕ Έξοδος
          </Link>
        </div>

        {/* Progress Bar */}
        <div className="w-full bg-gray-700 h-4 rounded-full overflow-hidden relative">
          <div
            className="bg-blue-500 h-full transition-all duration-500 ease-out"
            style={{ width: `${progressPercentage}%` }}
          ></div>
          <span className="absolute inset-0 flex items-center justify-center text-xs font-bold text-white drop-shadow">
            {progressPercentage}% (βάσει χρόνου)
          </span>
        </div>
      </div>

      {/* Main Content */}
      <div className="flex-1 overflow-y-auto p-6 max-w-3xl mx-auto w-full">
        <div className="mb-6 flex items-center gap-3">
          <span className="bg-blue-600 text-white w-10 h-10 flex items-center justify-center rounded-full text-xl font-bold">
            {currentStepIndex + 1}
          </span>
          <h1 className="text-3xl font-bold">{currentStep.title}</h1>
        </div>

        {/* Φωτογραφία Βήματος */}
        {currentStep.photoUrls && currentStep.photoUrls.length > 0 && (
          <div className="mb-6 rounded-xl overflow-hidden shadow-2xl border border-gray-700 bg-black">
            <img
              src={currentStep.photoUrls[0]}
              alt="Step"
              className="w-full h-auto object-contain max-h-80 mx-auto"
            />
          </div>
        )}

        <div className="bg-gray-800 p-6 rounded-xl border border-gray-700 mb-6 shadow-lg">
          <p className="text-xl leading-relaxed whitespace-pre-wrap">
            {currentStep.description}
          </p>
        </div>

        <div className="flex gap-4 mb-8">
          <div className="bg-gray-800 px-4 py-2 rounded border border-gray-700 flex items-center gap-2">
            <span>⏱️</span>
            <span className="font-bold text-xl">
              {currentStep.duration} λεπτά
            </span>
          </div>
        </div>

        {/* Υλικά Βήματος */}
        {currentStep.ingredients && currentStep.ingredients.length > 0 && (
          <div className="bg-blue-900 bg-opacity-20 p-5 rounded-xl border border-blue-800">
            <h3 className="text-blue-300 font-bold mb-3 uppercase text-sm tracking-wider">
              Υλικα για αυτο το βημα:
            </h3>
            <ul className="grid grid-cols-1 sm:grid-cols-2 gap-2">
              {currentStep.ingredients.map((ing, i) => (
                <li key={i} className="flex items-center gap-2 text-lg">
                  <span className="text-blue-400">●</span>
                  <span>
                    {ing.name}{" "}
                    <span className="font-bold text-blue-200">
                      {ing.quantity}
                      {ing.unit}
                    </span>
                  </span>
                </li>
              ))}
            </ul>
          </div>
        )}
      </div>

      {/* Footer Controls */}
      <div className="bg-gray-800 p-4 border-t border-gray-700 flex justify-between">
        <button
          onClick={() => setCurrentStepIndex((prev) => Math.max(0, prev - 1))}
          disabled={currentStepIndex === 0}
          className={`px-6 py-3 rounded-lg font-bold ${currentStepIndex === 0 ? "bg-gray-700 text-gray-500" : "bg-gray-600 hover:bg-gray-500 text-white"}`}
        >
          ← Προηγούμενο
        </button>

        <button
          onClick={handleNext}
          className={`px-8 py-3 rounded-lg font-bold text-white shadow-lg transform active:scale-95 transition ${
            currentStepIndex === steps.length - 1
              ? "bg-green-600 hover:bg-green-500"
              : "bg-blue-600 hover:bg-blue-500"
          }`}
        >
          {currentStepIndex === steps.length - 1
            ? "Ολοκλήρωση! 🎉"
            : "Επόμενο Βήμα →"}
        </button>
      </div>
    </div>
  );
}
